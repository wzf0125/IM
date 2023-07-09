package org.quanta.im.dto;

import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
public class SendMessageDTO {
    /**
     * 消息类型
     * 0 单聊
     * 1 群聊
     * */
    private Integer type;

    /**
     * 聊天id
     * */
    private Long chatId;

    /**
     * 消息内容
     * */
    private String message;
}
