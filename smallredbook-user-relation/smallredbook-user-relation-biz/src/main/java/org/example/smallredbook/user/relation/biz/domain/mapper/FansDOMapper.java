package org.example.smallredbook.user.relation.biz.domain.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.smallredbook.user.relation.biz.domain.dataobject.FansDO;

public interface FansDOMapper {
    /**
     * 根据userID 和 unfollowUserId 的联合索引删除 用户粉丝
     * @param userId
     * @param fansUserId
     * @return
     */
    int deleteByUserIdAndFansUserId(@Param("userId") Long userId,
                                    @Param("fansUserId") Long fansUserId);
    int deleteByPrimaryKey(Long id);

    int insert(FansDO record);

    int insertSelective(FansDO record);

    FansDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FansDO record);

    int updateByPrimaryKey(FansDO record);
}