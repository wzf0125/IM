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
@TableName("`group`")
public class Group {
    /**
     * 自增主键
     * */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     * */
    @TableField("`chat_id`")
    private Long chatId;

    /**
     * 群聊名称
     * */
    @TableField("`name`")
    private String name;

    /**
     * 群聊头像
     * */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 逻辑删除
     * 0 为未删除
     * 1 为已删除
     * */
    @TableLogic
    private Integer isDeleted;
}
