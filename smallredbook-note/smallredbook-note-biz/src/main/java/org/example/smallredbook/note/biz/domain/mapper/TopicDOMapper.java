package org.example.smallredbook.note.biz.domain.mapper;

import org.example.smallredbook.note.biz.domain.dataobject.TopicDO;

public interface TopicDOMapper {
    String selectNameByPrimaryKey(Long id);
    int deleteByPrimaryKey(Long id);

    int insert(TopicDO record);

    int insertSelective(TopicDO record);

    TopicDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TopicDO record);

    int updateByPrimaryKey(TopicDO record);
}