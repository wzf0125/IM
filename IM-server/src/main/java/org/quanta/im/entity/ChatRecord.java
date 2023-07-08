package org.quanta.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`chat_record`")
public class ChatRecord {
    /**
     * 自增主键
     * */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 聊天窗口id
     * */
    @TableField("`chat_id`")
    private String chatId;

    /**
     * 用户id
     * */
    @TableField("`user_id`")
    private String userId;

    /**
     * 消息类型
     * */
    @TableField("`type`")
    private Integer type;

    /**
     * 消息内容
     * */
    @TableField("`message`")
    String message;

    /**
     * 逻辑删除
     * 0 为未删除
     * 1 为已删除
     * */
    @TableLogic
    private Integer isDeleted;
}
