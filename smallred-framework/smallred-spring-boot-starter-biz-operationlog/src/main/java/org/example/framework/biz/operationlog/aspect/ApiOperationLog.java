package org.example.framework.biz.operationlog.aspect;

import java.lang.annotation.*;

/**
 * @Author: tzy
 * @Description: JSON 工具类，这在日志切面中打印出入参为 JSON 字符串会用到
 * @Date: 2024/7/5 10:27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiOperationLog {
    /**
     * API 功能描述
     *
     * @return
     */
    String description() default "";

}