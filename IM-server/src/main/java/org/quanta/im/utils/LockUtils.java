package org.quanta.im.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2023/7/9
 */
@Component
@AllArgsConstructor
public class LockUtils {
    private final RedisUtils redisUtils;

    public boolean lock(Long expire, String prefix, String... param) {
        String key = String.format(prefix, param);
        return this.lock(key, expire);
    }

    public boolean lock(String prefix, String... param) {
        String key = String.format(prefix, param);
        return this.lock(key);
    }

    public boolean lock(String key) {
        return this.lock(key, 30L);
    }

    public boolean lock(String key, Long expire) {
        return redisUtils.setNx(key, null, expire);
    }

    public boolean unlock(String key) {
        return redisUtils.releaseNx(key, null);
    }

    public boolean unlock(String prefix, String... param) {
        String key = String.format(prefix, param);
        return redisUtils.releaseNx(key, null);
    }
}
