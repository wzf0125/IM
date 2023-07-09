package org.quanta.im.constants;

import lombok.Getter;

/**
 * Description: 消息类型
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/8
 */
@Getter
public enum IMMessageType {
    // 聊天信息
    MESSAGE(0,"message"),
    // 聊天窗口更新
    CHAT_UPDATE(1,"chat_update"),
    // 异常
    ERROR(-1, "error");
    private final Integer code;
    private final String type;

    IMMessageType(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

}
