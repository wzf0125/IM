package org.quanta.im.handle;

import org.quanta.im.entity.User;
import org.quanta.im.utils.TokenUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 身份认证功能
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/8
 */
@Component
public class TokenHandle {

    @Resource
    TokenUtils tokenUtils;

    /**
     * 身份认证并返回用户信息
     * */
    public User auth(String token){
        return tokenUtils.retrieveToken(token);
    }
}
