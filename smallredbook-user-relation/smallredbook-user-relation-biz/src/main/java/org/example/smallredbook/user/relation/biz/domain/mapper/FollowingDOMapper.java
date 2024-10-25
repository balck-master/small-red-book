package org.example.smallredbook.user.relation.biz.domain.mapper;

import org.example.smallredbook.user.relation.biz.domain.dataobject.FollowingDO;

import java.util.List;

public interface FollowingDOMapper {
    /**
     * 根据 用户id 查询
     * @param userId
     * @return
     */
    List<FollowingDO> selectByUserId(Long userId);

    int deleteByPrimaryKey(Long id);

    int insert(FollowingDO record);

    int insertSelective(FollowingDO record);

    FollowingDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FollowingDO record);

    int updateByPrimaryKey(FollowingDO record);
}