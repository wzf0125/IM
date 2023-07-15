package org.quanta.im.controller;

import lombok.AllArgsConstructor;
import org.quanta.im.bean.Response;
import org.quanta.im.dto.LoginDTO;
import org.quanta.im.dto.RegisterDTO;
import org.quanta.im.entity.User;
import org.quanta.im.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    /**
     *  登陆(单体)
     *  POST /user/login
     *  接口ID：94209784
     *  接口地址：https://app.apifox.com/link/project/2952958/apis/api-94209784
     * */
    @PostMapping("/login")
    public Response<Object> login(@Validated @RequestBody LoginDTO param){
        return Response.success(userService.login(param.getUsername(), param.getPassword()));
    }

    /**
     *  注册(单体)
     *  POST /user/register
     *  接口ID：94209796
     *  接口地址：https://app.apifox.com/link/project/2952958/apis/api-94209796
     * */
    @PostMapping("/register")
    public Response<Object> register(@Validated @RequestBody RegisterDTO param){
        User user = new User();
        BeanUtils.copyProperties(param,user);
        userService.register(user);
        return Response.success();
    }

    /**
     * 获取用户列表
     * GET /user/list
     * 接口ID：94209809
     * 接口地址：https://app.apifox.com/link/project/2952958/apis/api-94209809
     * */
    @GetMapping("/list")
    public Response<Object> userList(){
        List<User> userList = userService.list();
        return Response.success(userList);
    }
}
