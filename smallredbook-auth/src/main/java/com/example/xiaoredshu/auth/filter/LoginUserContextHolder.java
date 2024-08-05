package com.example.xiaoredshu.auth.filter;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.example.framework.common.constant.GlobalConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: tzy
 * @Description:  登录用户上下文
 * @Date: 2024/8/4 22:24
 */
public class LoginUserContextHolder {

    //初始化一个ThreadLocal对象
    private static final TransmittableThreadLocal <Map<String, Object>>LOGIN_USER_CONTEXT_THREAD_LOCAL = TransmittableThreadLocal.withInitial(HashMap::new);

    /**
     * 获取用户ID
     * @param value
     */
    public static void setUserId(Object value){
        LOGIN_USER_CONTEXT_THREAD_LOCAL.get().put(GlobalConstants.USER_ID,value);

    }

    /**
     * 获取用户 ID
     *
     * @return
     */
    public static Long getUserId(){
        Object value = LOGIN_USER_CONTEXT_THREAD_LOCAL.get().get(GlobalConstants.USER_ID);
        if(Objects.isNull(value)){
            return null;
        }
        return Long.valueOf(value.toString());
    }

    /**
     * 删除 ThreadLocal
     * 防止内存泄漏，在线程结束时清除 ThreadLocal 变量
     */
    public static void remove() {
        LOGIN_USER_CONTEXT_THREAD_LOCAL.remove();
    }
}
