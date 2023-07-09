package org.quanta.im.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Data
public class LoginDTO {
    /**
     * 账号
     * */
    @NotNull(message = "账号不能为空")
    private String username;

    /**
     * 密码
     * */
    @NotNull(message = "密码不能为空")
    private String password;
}
