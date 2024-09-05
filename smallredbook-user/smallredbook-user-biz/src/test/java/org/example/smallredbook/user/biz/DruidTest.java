package org.example.smallredbook.user.biz;

import com.alibaba.druid.filter.config.ConfigTools;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.framework.common.utils.JsonUtils;
import org.example.smallredbook.user.biz.domain.dataobject.UserDO;
import org.example.smallredbook.user.biz.domain.mapper.UserDOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author: tzy
 * @Description:
 * @Date: 2024/7/7 20:02
 */


@SpringBootTest
@Slf4j
public class DruidTest {

    @Resource
    private UserDOMapper userDOMapper;

    /**
     *  Druid加密
     */
    @Test
    @SneakyThrows
    void testEncodePassword(){
//        String password = "123456";
        String password = "956764c5e473f4d3";
        String[] arr = ConfigTools.genKeyPair(512);

        //
        // 私钥
        log.info("privateKey: {}", arr[0]);
        // 公钥
        log.info("publicKey: {}", arr[1]);

        String encodePassword = ConfigTools.encrypt(arr[0], password);
        log.info("加密后的密码: {}", encodePassword);
        String decrypt = ConfigTools.decrypt(arr[1], encodePassword);
        log.info("解密后的密码：{}",decrypt);

//        privateKey: MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAj8BzfSdG/eBapQLz6u6nTLP93zbK0i8Vcv3+oW2ednPoCpxiaCnw6zIeETJ8v9U3AdfWQuQB+dBBy8FC/lFFLQIDAQABAkAL0OrtT6wzNBzfUXXRStqvoF/u2Cclzb76m8wxYrxSyiXr6Vjb2IkYRGZURtxgwkPYAi45mcb2BBXpBzq0gyz1AiEAmfGevGrt4wJ2jjae9AlcHg85osf4K55BWQBFuqec4BMCIQDvDRj7/pgPM5qBhIFZfoJiEh+v2upKOcSczVRKz2ptvwIhAIFoFgrHTE+++1AS5E26LIBsRgPrW1e3QbGCRBti2MGXAiEA5qeJmuP9znUpOJ0kvbhej8gTAPhGphX1SqKoGYLmmzsCIQCN7WhLFdSfgN6go8GoMDTTcL9vZl+/2TqmeSBDqdCaCA==
//        密码为：123456时
//        publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI/Ac30nRv3gWqUC8+rup0yz/d82ytIvFXL9/qFtnnZz6AqcYmgp8OsyHhEyfL/VNwHX1kLkAfnQQcvBQv5RRS0CAwEAAQ==
//        加密后的密码：A2qT03X7KlL4v/F2foD6kV/Ch9gpNBWOh1qoCywanjv1AsI7f9x3iAyR9NkUKeV+FMo+halCTzy5Llbk2VOrVQ==

//        密码为：956764c5e473f4d3
//        publicKey: MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI8JGMGfXsNXGLtEG9BPrloN4YKCI88Ihn8gJzaokCqB5m0iV3Q2UoEv/M9iFyd9S9BO6rVqP5QpaFybyaW63MkCAwEAAQ==
//        加密后的密码：ImBl6+6zoFuywJKzwo1UWkyRa8AhGj/pfzkDZPzn3cGWVyv3SbV8YoWoGLJ0jJslc8yWFmVv80JQWMwXg5V6Fg==
    }

    @Test
    void testSelect(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(1L);
        log.info("user:{}", JsonUtils.toJsonString(userDO));
    }
}
