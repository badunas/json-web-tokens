package org.badun.jwtdemo.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artsiom Badun.
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Optional<String> getValue(String key) {
        String result = stringRedisTemplate.opsForValue().get(key);
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    public void setValue(String key, String data, long ttl, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, data, ttl, timeUnit);
    }

    public boolean isExists(String key) {
        return getValue(key).isPresent();
    }
}
