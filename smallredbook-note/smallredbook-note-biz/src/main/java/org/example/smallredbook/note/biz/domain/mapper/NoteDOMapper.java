package org.example.smallredbook.note.biz.domain.mapper;

import org.example.smallredbook.note.biz.domain.dataobject.NoteDO;

public interface NoteDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteDO record);

    int insertSelective(NoteDO record);

    NoteDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteDO record);

    int updateByPrimaryKey(NoteDO record);

    /**
     * 更新笔记状态为 仅自己可见
     * @param noteDO
     * @return
     */
    int updateVisibleOnlyMe(NoteDO noteDO);

    /**
     * 更新笔记置顶状态
     * @param noteDO
     * @return
     */
    int updateIsTop(NoteDO noteDO);
}