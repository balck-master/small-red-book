package org.example.smallredbook.user.relation.biz.domain.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.smallredbook.user.relation.biz.domain.dataobject.FollowingDO;

import java.util.List;

public interface FollowingDOMapper {
    /**
     * 根据userID 和 unfollowUserId 的联合索引删除关注用户
     * @param userId
     * @param unfollowUserId
     * @return
     */
    int deleteByUserIdAndFollowingUserId(@Param("userId") Long userId,
                                         @Param("unfollowUserId") Long unfollowUserId);
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