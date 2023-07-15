package org.quanta.im.constants;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/15
 */
public enum ChatType {
    // 私聊类型
    WHISPER(0,"whisper"),

    // 群聊类型
    GROUP(1,"group");

    private Integer code;
    private String name;

    ChatType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
