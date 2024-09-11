package org.example.smallredbook.note.biz.rpc;

import jakarta.annotation.Resource;
import org.example.framework.common.response.Response;
import org.example.smallredbook.kv.api.KeyValueFeignApi;
import org.example.smallredbook.kv.dto.req.AddNoteContentReqDTO;
import org.example.smallredbook.kv.dto.req.DeleteNoteContentReqDTO;
import org.example.smallredbook.kv.dto.req.FindNoteContentReqDTO;
import org.example.smallredbook.kv.dto.rsp.FindNoteContentRspDTO;
import org.example.smallredbook.note.biz.model.vo.FindNoteDetailReqVO;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: tzy
 * @Description: kv 键值服务
 * @Date: 2024/9/7 15:08
 */
@Component
public class KeyValueRpcService {
    @Resource
    private KeyValueFeignApi keyValueFeignApi;

    /**
     * 保存笔记内容
     * @param uuid
     * @param content
     * @return
     */
    public boolean saveNoteContent(String uuid , String content){
        AddNoteContentReqDTO addNoteContentReqDTO = new AddNoteContentReqDTO();
        addNoteContentReqDTO.setUuid(uuid);
        addNoteContentReqDTO.setContent(content);

        Response<?> response = keyValueFeignApi.addNoteContent(addNoteContentReqDTO);
        if(Objects.isNull(response) || !response.isSuccess()){
            return false;
        }

        return true;
    }

    public boolean deleteNoteContent(String uuid){
        DeleteNoteContentReqDTO deleteNoteContentReqDTO = new DeleteNoteContentReqDTO();
        deleteNoteContentReqDTO.setUuid(uuid);

        Response<?> response = keyValueFeignApi.deleteNoteContent(deleteNoteContentReqDTO);
        if(Objects.isNull(response) || !response.isSuccess()){
            return false;
        }

        return true;
    }

    /**
     * 查询笔记内容
     * @param uuid
     * @return
     */
    public String findNoteContent(String uuid){
        FindNoteContentReqDTO findNoteContentReqDTO = new FindNoteContentReqDTO();
        findNoteContentReqDTO.setUuid(uuid);
        Response<FindNoteContentRspDTO> response = keyValueFeignApi.findNoteContent(findNoteContentReqDTO);

        if(Objects.isNull(response) || !response.isSuccess() || Objects.isNull(response.getData())){
            return null;
        }
        return response.getData().getContent();
    }
}
