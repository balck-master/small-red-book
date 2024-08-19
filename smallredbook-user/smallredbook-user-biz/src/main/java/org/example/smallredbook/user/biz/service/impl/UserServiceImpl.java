package org.example.smallredbook.user.biz.service.impl;




import com.alibaba.cloud.commons.lang.StringUtils;
import com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.enums.DeletedEnum;
import org.example.framework.common.enums.StatusEnum;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.JsonUtils;
import org.example.framework.common.utils.ParamUtils;
import org.example.smallredbook.oss.api.FileFeignApi;
import org.example.smallredbook.user.api.dto.req.FindUserByPhoneReqDTO;
import org.example.smallredbook.user.api.dto.req.RegisterUserReqDTO;
import org.example.smallredbook.user.api.dto.resp.FindUserByPhoneRspDTO;
import org.example.smallredbook.user.biz.constant.RedisKeyConstants;
import org.example.smallredbook.user.biz.constant.RoleConstants;
import org.example.smallredbook.user.biz.domain.dataobject.RoleDO;
import org.example.smallredbook.user.biz.domain.dataobject.UserDO;
import org.example.smallredbook.user.biz.domain.dataobject.UserRoleDO;
import org.example.smallredbook.user.biz.domain.mapper.RoleDOMapper;
import org.example.smallredbook.user.biz.domain.mapper.UserDOMapper;
import org.example.smallredbook.user.biz.domain.mapper.UserRoleDOMapper;
import org.example.smallredbook.user.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.user.biz.enums.SexEnum;
import org.example.smallredbook.user.biz.model.vo.UpdateUserInfoReqVO;
import org.example.smallredbook.user.biz.rpc.OssRpcService;
import org.example.smallredbook.user.biz.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: tzy
 * @Description: 用户服务实现类
 * @Date: 2024/8/14 14:11
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private FileFeignApi fileFeignApi;
    @Resource
    private OssRpcService ossRpcService;

    @Resource
    private UserRoleDOMapper userRoleDOMapper;
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO) {
        UserDO userDO = new UserDO();

        userDO.setId(LoginUserContextHolder.getUserId());
        //标识位,是否需要更新
        boolean needUpdate = false;

        //头像
        MultipartFile avatarFile = updateUserInfoReqVO.getAvater();
        if(Objects.isNull(avatarFile)){
            //todo: 调用对象存储服务上传文件
            String avatar = ossRpcService.uploadFile(avatarFile);
            log.info("==> 调用oss服务成功，上传头像，url:{}",avatar);

            //若上传头像失败，则抛出业务异常
            if(StringUtils.isBlank(avatar)){
                throw new BizException(ResponseCodeEnum.UPLOAD_AVATAR_FAIL);
            }
            userDO.setAvatar(avatar);
            needUpdate = true;
        }

        //昵称
        String nickname = updateUserInfoReqVO.getNickname();
        if(StringUtils.isNotBlank(nickname)){
            Preconditions.checkArgument(ParamUtils.checkNickname(nickname) , ResponseCodeEnum.NICK_NAME_VALID_FAIL.getErrorMessage());
            userDO.setNickname(nickname);
            needUpdate = true;
        }

        //小哈书号
        String xiaohashuId = updateUserInfoReqVO.getXiaohashuId();
        if(StringUtils.isNotBlank(xiaohashuId)){
            Preconditions.checkArgument(ParamUtils.checkXiaohashuId(xiaohashuId),ResponseCodeEnum.XIAOHASHU_ID_VALID_FAIL.getErrorMessage());
            userDO.setXiaohashuId(xiaohashuId);
            needUpdate = true;
        }

        //性别
        Integer sex = updateUserInfoReqVO.getSex();
        if(Objects.nonNull(sex)){
            Preconditions.checkArgument(SexEnum.isValid(sex),ResponseCodeEnum.SEX_VALID_FAIL.getErrorMessage());
            userDO.setSex(sex);
            needUpdate = true;
        }

        //生日
        LocalDateTime birthday = updateUserInfoReqVO.getBirthday();
        if(Objects.nonNull(birthday)){
            userDO.setBirthday(birthday);
            needUpdate = true;
        }

        //个人简介
        String introduction = updateUserInfoReqVO.getIntroduction();
        if(StringUtils.isNotBlank(introduction)){
            Preconditions.checkArgument(ParamUtils.checkLength(introduction,100),ResponseCodeEnum.INTRODUCTION_VALID_FAIL.getErrorMessage());
            userDO.setIntroduction(introduction);
            needUpdate = true;
        }

        //背景图
        MultipartFile backgroundImgFile = updateUserInfoReqVO.getBackgroundImg();
        if(Objects.nonNull(backgroundImgFile)){
            //todo:调用对象存储服务上传文件
            String backgroundImg = ossRpcService.uploadFile(backgroundImgFile);
            log.info("==> 调用oss服务成功，上传背景图，url：{}",backgroundImg);

            if(StringUtils.isBlank(backgroundImg)){
                throw new BizException(ResponseCodeEnum.UPLOAD_BACKGROUND_IMG_FAIL);
            }

            userDO.setBackgroundImg(backgroundImg);
            needUpdate = true;
        }

        if(needUpdate){
            //更新用户信息
            userDO.setUpdateTime(LocalDateTime.now());
            userDOMapper.updateByPrimaryKeySelective(userDO);
        }

        return Response.success();
    }

    /**
     * 注册
     * @param registerUserReqDTO
     * @return
     */
    public Response<Long> register(RegisterUserReqDTO registerUserReqDTO) {
        String phone = registerUserReqDTO.getPhone();

        // 先判断该手机号是否已被注册
        UserDO userDO1 = userDOMapper.selectByPhone(phone);

        log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO1));

        // 若已注册，则直接返回用户 ID
        if (Objects.nonNull(userDO1)) {
            return Response.success(userDO1.getId());
        }

        // 否则注册新用户
        // 获取全局自增的小哈书 ID
        Long xiaohashuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_GENERATOR_KEY);

        UserDO userDO = UserDO.builder()
                .phone(phone)
                .xiaohashuId(String.valueOf(xiaohashuId)) // 自动生成小红书号 ID
                .nickname("小红薯" + xiaohashuId) // 自动生成昵称, 如：小红薯10000
                .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                .build();

        // 添加入库
        userDOMapper.insert(userDO);

        // 获取刚刚添加入库的用户 ID
        Long userId = userDO.getId();

        // 给该用户分配一个默认角色
        UserRoleDO userRoleDO = UserRoleDO.builder()
                .userId(userId)
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue())
                .build();
        userRoleDOMapper.insert(userRoleDO);

        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

        // 将该用户的角色 ID 存入 Redis 中
        List<String> roles = new ArrayList<>(1);
        roles.add(roleDO.getRoleKey());

        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

        return Response.success(userId);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param findUserByPhoneReqDTO
     * @return
     */
    @Override
    public Response<FindUserByPhoneRspDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO) {
        String phone = findUserByPhoneReqDTO.getPhone();

        // 根据手机号查询用户信息
        UserDO userDO = userDOMapper.selectByPhone(phone);

        // 判空
        if (Objects.isNull(userDO)) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        // 构建返参
        FindUserByPhoneRspDTO findUserByPhoneRspDTO = FindUserByPhoneRspDTO.builder()
                .id(userDO.getId())
                .password(userDO.getPassword())
                .build();

        return Response.success(findUserByPhoneRspDTO);
    }
}


