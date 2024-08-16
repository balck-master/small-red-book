package org.example.smallredbook.user.biz.service.impl;


import com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.framework.biz.context.holder.LoginUserContextHolder;
import org.example.framework.common.exception.BizException;
import org.example.framework.common.response.Response;
import org.example.framework.common.utils.ParamUtils;
import org.example.smallredbook.oss.api.FileFeignApi;
import org.example.smallredbook.user.biz.domain.dataobject.UserDO;
import org.example.smallredbook.user.biz.domain.mapper.UserDOMapper;
import org.example.smallredbook.user.biz.enums.ResponseCodeEnum;
import org.example.smallredbook.user.biz.enums.SexEnum;
import org.example.smallredbook.user.biz.model.vo.UpdateUserInfoReqVO;
import org.example.smallredbook.user.biz.rpc.OssRpcService;
import org.example.smallredbook.user.biz.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.time.LocalDateTime;
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
}
