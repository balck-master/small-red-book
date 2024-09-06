package org.example.smallredbook.distributed.id.generator.constant;

import org.example.smallredbook.distributed.id.generator.api.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/9/5 13:59
 */
@FeignClient(name = ApiConstants.SERVICE_NAME  )
public interface DistributedIdGeneratorFeignApi {

    String PREFIX = "/id";

    @GetMapping(value = PREFIX + "/segment/get/{key}")
    String getSegmentId(@PathVariable("key") String key);

    @GetMapping(value = PREFIX + "/snowflake/get/{key}")
    String getSnowflakeId(@PathVariable("key") String key);

}
