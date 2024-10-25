package org.example.framework.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @Author: tzy
 * @Description: 日期工具类
 * @Date: 2024/10/25 16:58
 */
public class DateUtils {

    /**
     * LocalDateTime 转时间戳
     * @param localDateTime
     * @return
     */
    public static long localDateTime2Timestamp(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
