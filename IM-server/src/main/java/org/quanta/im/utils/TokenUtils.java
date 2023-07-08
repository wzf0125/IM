package org.quanta.im.utils;

import org.quanta.im.entity.User;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Description: 返回值枚举类型
 * Param:
 * return:
 * Author: wzf mqh hzq zjx
 * Date: 2022/12/9
 */
@Component
public class TokenUtils {

    private final RedisUtils redisUtils;

    public TokenUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    private final String prefix = "user_%s";

    /**
     * 发放token
     *
     * @param user 用户信息
     * @return 获取到的token
     */
    public String grantToken(User user) {
        String formerToken = (String) redisUtils.get(String.format(prefix, user.getId()));
        // 用户已经分发token且该token仍在有效期内
        if (formerToken != null && redisUtils.get(formerToken) != null) {
            refreshToken(formerToken);
            return formerToken;
        }
        // 新分发token逻辑
        String token = UUID.randomUUID().toString().replace("-", "");
        // token有效期为1周
        boolean result = redisUtils.set(token, user, 60 * 60 * 24 * 7);
        redisUtils.set(String.format(prefix, user.getId()), token);
        if (!result) {
            throw new RuntimeException("token非法");
        }
        return token;
    }

    /**
     * 获取token信息
     *
     * @param key token
     * @return uid和role的map
     */
    @SuppressWarnings("unchecked")
    public User retrieveToken(String key) {
        return (User) redisUtils.get(key);
    }

    /**
     * 获取token对应的id
     *
     * @param key token
     * @return id
     */
    public long getTokenUid(String key) {
        return (long) retrieveToken(key).getId();
    }

    /**
     * 删除token
     *
     * @param key 缓存key
     */
    private void destroyToken(String key) {
        redisUtils.del(key);
    }

    /**
     * 刷新token时间
     */
    public void refreshToken(String token) {
        Long last = redisUtils.getExpire(token); // 获取token剩余时间
        if (last <= (60 * 60 * 24)) { // 有效期小于一天
            // 更新有效期
            redisUtils.setExpire(token, 60 * 60 * 24 * 7);
        }
    }

    /**
     * 安全退出
     *
     * @param uid uid
     */
    public void safeExit(long uid) {
        String formerToken = (String) redisUtils.get(String.format(prefix, uid));
        redisUtils.del(String.format(prefix, uid));
        destroyToken(formerToken);
    }
}
