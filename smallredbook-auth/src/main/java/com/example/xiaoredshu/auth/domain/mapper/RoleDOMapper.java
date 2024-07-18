package com.example.xiaoredshu.auth.domain.mapper;

import com.example.xiaoredshu.auth.domain.dataobject.RoleDO;

public interface RoleDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);
}