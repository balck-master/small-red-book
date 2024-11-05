package org.example.smallredbook.user.biz.domain.mapper;


import io.lettuce.core.dynamic.annotation.Param;
import org.example.smallredbook.user.biz.domain.dataobject.RolePermissionDO;

import java.util.List;

public interface RolePermissionDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionDO record);

    int insertSelective(RolePermissionDO record);

    RolePermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionDO record);

    int updateByPrimaryKey(RolePermissionDO record);

    /**
     * 根据角色 ID 集合批量查询
     * @return
     */
    List<RolePermissionDO> selectByRoleIds(@Param("roleIds") List<Long> rolesIds);
}