package org.quanta.im.controller;

import lombok.extern.log4j.Log4j2;
import org.quanta.eas.annotation.Log;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/10
 */
@Log4j2
public class BaseController {
    /**
     * 获取目前用户的id和role
     *
     * @return id和role数组
     */
    private Map<String, Object> getCurrentUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Map<String, Object> result = new HashMap<>();
        result.put("uid", request.getAttribute("uid"));
        return result;
    }

    /**
     * 获取用户uid
     *
     * @return uid
     */
    public long getUid() {
        return (long) getCurrentUser().get("uid");
    }
}
