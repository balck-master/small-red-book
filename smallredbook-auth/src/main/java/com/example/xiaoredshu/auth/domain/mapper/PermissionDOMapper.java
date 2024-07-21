package com.example.xiaoredshu.auth.domain.mapper;

import com.example.xiaoredshu.auth.domain.dataobject.PermissionDO;

import java.util.List;

public interface PermissionDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PermissionDO record);

    int insertSelective(PermissionDO record);

    PermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PermissionDO record);

    int updateByPrimaryKey(PermissionDO record);

    /**
     * 查询 APP 端所有被启用的权限
     * type = 3,按钮，普通用户点击
     * @return
     */
    List<PermissionDO> selectAppEnabledList();
}