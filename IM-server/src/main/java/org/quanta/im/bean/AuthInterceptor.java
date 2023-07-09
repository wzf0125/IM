package org.quanta.im.bean;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.quanta.im.utils.TokenUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: 返回值枚举类型
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/07/09
 */
@Log4j2
public class AuthInterceptor implements HandlerInterceptor {

    public static final String TOKEN_HEADER = "Authorization";

    @Resource
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否是OPTIONS 是则放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("OPTIONS: ");
            return true;
        }

        // 设置响应头为json
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        // 设置响应头编码
        response.setCharacterEncoding("UTF-8");
        // 无token参数拒绝，放通的接口除外
        if (request.getHeader(TOKEN_HEADER) == null || request.getHeader(TOKEN_HEADER).isEmpty()) {
            log.debug("");
            response.getWriter().write(JSON.toJSONString(Response.unauthorized("缺少token参数")));
            return false;
        }
        String token = request.getHeader(TOKEN_HEADER);
        if (token == null) {
            response.getWriter().write(JSON.toJSONString(Response.unauthorized()));
        }
        long uid;
        try {
            uid = tokenUtils.getTokenUid(token);
        } catch (Exception e) {
            response.getWriter().write(JSON.toJSONString(Response.unauthorized(e.getMessage())));
            return false;
        }

        // 刷新token
        tokenUtils.refreshToken(token);
        // 权限塞request里传给controller
        request.setAttribute("uid", uid);
        return true;
    }

}
