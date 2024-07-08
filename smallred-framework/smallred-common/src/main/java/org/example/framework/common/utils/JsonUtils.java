package org.example.framework.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.SneakyThrows;
import org.example.framework.common.constant.DateConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/5 10:22
 */
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
}
