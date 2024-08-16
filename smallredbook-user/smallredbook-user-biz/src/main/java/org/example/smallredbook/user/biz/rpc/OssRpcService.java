package org.example.smallredbook.user.biz.rpc;

import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
import org.example.smallredbook.oss.api.FileFeignApi;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: tzy
 * @Description: 对象存储服务调用
 * @Date: 2024/8/15 22:03
 */
@Component
public class OssRpcService {

    @Resource
    private FileFeignApi fileFeignApi;
    public String uploadFile(MultipartFile file){
        //调用对象存储服务上传文件
        Response<?> response = fileFeignApi.uploadFile(file);

        if(!response.isSuccess()){
            return null;
        }

        //返回图片访问链接
        return (String) response.getData();
    }

}
