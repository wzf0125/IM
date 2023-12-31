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
 * Date: 2023/7/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`user`")
public class User {
    /**
     * 自增主键
     * */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * */
    @TableField("`username`")
    private String username;

    /**
     * 密码
     * */
    @TableField("`password`")
    private String password;

    /**
     * 头像
     * */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 是否通过验证
     * */
    @TableField("`is_valid`")
    private Integer isValid;

    /**
     * 逻辑删除
     * 0 为未删除
     * 1 为已删除
     * */
    @TableLogic
    private Integer isDeleted;
}
