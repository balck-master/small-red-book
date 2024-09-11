package org.example.smallredbook.note.biz.constant;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/9/11 15:58
 */
public class RedisKeyConstants {
    /**
     * 笔记详情 KEY 前缀
     */
    public static final String NOTE_DETAIL_KEY = "note:detail:";


    /**
     * 构建完整的笔记详情 KEY
     * @param noteId
     * @return
     */
    public static String buildNoteDetailKey(Long noteId) {
        return NOTE_DETAIL_KEY + noteId;
    }
}
