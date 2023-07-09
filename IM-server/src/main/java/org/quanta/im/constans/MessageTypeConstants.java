package org.quanta.im.constans;

import lombok.Getter;

/**
 * Description: 消息类型
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/8
 */
@Getter
public enum MessageTypeConstants {
    // 用户详情
    USER_INFO(0, "user_info"),
    // 用户列表
    USER_LIST(1, "user_list"),
    // 获取聊天列表
    CHAT_LIST(3,"chat_list"),
    // 聊天记录
    CHAT_RECORD(4, "chat_record"),
    // 进入聊天室
    ENTER_CHAT_ROOM(5, "enter_chat_room"),
    // 进入私聊
    ENTER_CHAT(6,"enter_chat"),
    // 发送私聊消息
    SEND_WHISPER(7,"send_whisper"),
    // 发送群聊消息
    SEND_BROADCAST(8,"send_broadcast"),
    // 异常
    ERROR(-1, "error");
    private final Integer code;
    private final String type;

    MessageTypeConstants(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

}
