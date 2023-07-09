package org.quanta.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.quanta.im.dto.SendMessageDTO;
import org.quanta.im.entity.Chat;
import org.quanta.im.vo.ChatListVo;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
public interface ChatService extends IService<Chat> {
    // 发送消息
    void sendMessage(Long uid,SendMessageDTO param);

    // 创建私聊窗口哦
    void createWhisper(Long uid,Long targetUid);

    // 创建群组聊天
    void createGroup(Long uid,String groupName, List<Long> targetList);

    // 获取聊天室列表
    ChatListVo getChatList(Long uid);
}
