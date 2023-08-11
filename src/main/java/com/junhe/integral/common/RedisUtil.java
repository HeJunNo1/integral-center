package com.junhe.integral.common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/26
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public void increment(String key, int value){
        redisTemplate.opsForValue().increment(key, value);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public void hSetAll(String key, Map<String, Object> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    public Object hget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    public Set<Object> hkeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void hdel(String key, Object... field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    public void hsetnx(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void createStreamConsumerGroup(String key, String groupName) {
        redisTemplate.opsForStream().createGroup(key, ReadOffset.from("0"),groupName);
    }

    public void execute(String script, List<String> keys, Object ... args) {
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/"+script+".lua")));
        defaultRedisScript.setResultType(Object.class);
        redisTemplate.execute(defaultRedisScript, keys, args);
    }

    public void streamAdd(String stream, String key,Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        MapRecord<String, String, Object> record = MapRecord.create(stream, map);
        redisTemplate.opsForStream().add(record);
    }

    public void streamACK(String key, String group, String recordId){
        redisTemplate.opsForStream().acknowledge(key, group, recordId);
    }

}
