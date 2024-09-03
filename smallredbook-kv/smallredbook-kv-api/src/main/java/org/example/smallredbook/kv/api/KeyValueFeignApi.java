package org.example.smallredbook.kv.api;

import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.constant.ApiConstants;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: tzy
 * @Description: k-v 键值存储 Feign 接口
 * @Date: 2024/9/3 21:35
 */
@FeignClient(name = ApiConstants.SERVICE_NAME)
public interface KeyValueFeignApi {
    String PREFIX = "/kv";
    @PostMapping(value = PREFIX+"/note/content/add")
    Response<?> addNoteContent(@RequestBody AddNoteContentReqDTO addNoteContentReqDTO);
}
