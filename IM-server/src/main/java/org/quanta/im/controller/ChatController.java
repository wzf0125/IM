package org.quanta.im.controller;

import lombok.AllArgsConstructor;
import org.quanta.im.bean.Response;
import org.quanta.im.dto.CreateGroupDTO;
import org.quanta.im.dto.SendMessageDTO;
import org.quanta.im.service.ChatService;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController extends BaseController{
    private final ChatService chatService;
    /**
     *  向聊天发送消息
     *  POST /chat/send
     *  接口ID：94217185
     *  接口地址：https://app.apifox.com/link/project/2952958/apis/api-94217185
     * */
    @PostMapping("/send")
    public Response<Object> sendMessage(@RequestBody SendMessageDTO param){
        chatService.sendMessage(getUid(), param);
        return Response.success();
    }

    /**
     *  发起单独聊天
     *  POST /chat/whisper
     *  接口ID：94218856
     *  接口地址：https://app.apifox.com/link/project/2952958/apis/api-94218856
     * */
    @PostMapping("/whisper")
    public Response<Object> createWhisper(@RequestParam Long targetUid){
        chatService.createWhisper(getUid(),targetUid);
        return Response.success();
    }

    /**
     *  发起群组聊天
     *  POST /chat/group
     *  接口ID：94218886
     *  接口地址：https://app.apifox.com/link/project/2952958/apis/api-94218886
     * */
    @PostMapping("/group")
    public Response<Object> createGroup(@RequestBody CreateGroupDTO param){
        chatService.createGroup(getUid(),param.getGroupName(),param.getIds());
        return Response.success();
    }

    /**
     * 获取聊天记录
     * */
    @GetMapping("/{id}")
    public Response<Object> getChatRecord(@PathVariable Long id){
        return Response.success();
    }

    /**
     * 获取聊天列表
     * */
    @GetMapping("/list")
    public Response<Object> getChatList(){
        return Response.success(chatService.getChatList(getUid()));
    }
}
