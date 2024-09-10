package org.example.framework.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

public class JsonUtils {
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

//    static {
//        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        OBJECT_MAPPER.registerModules(new JavaTimeModule()); // 解决 LocalDateTime 的序列化问题
//    }


    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //JacksonConfig里面里面已经写了 ,解决 LocalDateTime 的序列化问题
       OBJECT_MAPPER.registerModules(new JavaTimeModule());

    }

    /**
     * 初始化：统一使用 Spring Boot 个性化配置的 ObjectMapper
     * @param objectMapper
     */
    public static void init(ObjectMapper objectMapper){
        OBJECT_MAPPER = objectMapper;
    }

    /**
     *  将对象转换为 JSON 字符串
     * @param obj
     * @return
     */
    @SneakyThrows
    public static String toJsonString(Object obj) {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     *  将 JSON 字符串转换为对象
     * @param jsonStr 对象类的 JSON 字符串
     * @param clazz 想要转换的目标类
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> T parseObject(String jsonStr , Class<T> clazz){
        if(StringUtils.isBlank(jsonStr)){
            return null;
        }
        return OBJECT_MAPPER.readValue(jsonStr,clazz);
    }
}