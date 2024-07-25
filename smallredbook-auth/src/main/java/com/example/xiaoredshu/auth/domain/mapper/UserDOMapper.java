package com.example.xiaoredshu.auth.domain.mapper;

import com.example.xiaoredshu.auth.domain.dataobject.UserDO;

public interface UserDOMapper {
    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    UserDO selectByPhone(String phone);
    int deleteByPrimaryKey(Long id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}