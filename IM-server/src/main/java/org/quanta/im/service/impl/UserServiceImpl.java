package org.quanta.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.quanta.im.constants.LockPrefix;
import org.quanta.im.entity.User;
import org.quanta.im.exception.ApiException;
import org.quanta.im.mapper.UserMapper;
import org.quanta.im.service.UserService;
import org.quanta.im.utils.LockUtils;
import org.quanta.im.utils.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * Author: wzf
 * Date: 2023/7/8
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final TokenUtils tokenUtils;
    private final LockUtils lockUtils;

    @Override
    public String login(String username, String password) {
        User user = getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getPassword, password)
        );
        if (user == null) {
            throw new ApiException("账号或密码错误");
        }
        return tokenUtils.grantToken(user);
    }

    @Override
    @Transactional
    public void register(User param) {
        User user = getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, param.getUsername())
        );
        if (user != null) {
            throw new ApiException("用户名已被注册");
        }

        if (lockUtils.lock(LockPrefix.REGISTER_LOCK, param.getUsername())) {
            // 注册用户
            save(param);
            // 解锁
            lockUtils.unlock(LockPrefix.REGISTER_LOCK, param.getUsername());
        } else {
            throw new ApiException("操作频繁");
        }
    }
}
