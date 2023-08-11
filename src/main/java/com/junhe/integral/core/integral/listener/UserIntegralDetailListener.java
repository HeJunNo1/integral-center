package com.junhe.integral.core.integral.listener;

import com.alibaba.fastjson.JSONObject;
import com.junhe.integral.common.RedisUtil;
import com.junhe.integral.constant.RedisKeys;
import com.junhe.integral.core.integral.dao.UserIntegralAddFailureDao;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;
import com.junhe.integral.core.integral.service.UserIntegralService;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户积分变更监听器
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/3
 */
@Component
@Slf4j
public class UserIntegralDetailListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    private UserIntegralService userIntegralService;
    @Resource
    private UserIntegralAddFailureDao userIntegralAddFailureDao;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        try {
            IntegralDetailDTO integralDetailDTO = JSONObject.parseObject(message.getValue().get("record"), IntegralDetailDTO.class);
            userIntegralService.asyncPersistenceIntegral(integralDetailDTO);
            redisTemplate.opsForStream().acknowledge(RedisKeys.INTEGRAL_ASYNC_PERSISTENCE_GROUP, message);
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                return connection.xDel(RedisKeys.INTEGRAL_STREAM.getBytes(), message.getId());
            });
        } catch (Exception e){
            e.printStackTrace();
            retry(message);
        }
    }

    /**
     * 消费失败重试3次，若还失败，则记录日志人工处理
     * @author HEJUN
     * @date 2023/7/3
     */
    private void retry(MapRecord<String, String, String> message) {
        String retryTimes = message.getValue().get("retryTimes");
        if (StringUtils.isBlank(retryTimes)){
            message.getValue().put("retryTimes", "1");
            redisTemplate.opsForStream().add(message);
        } else if (Integer.valueOf(retryTimes) <= 3){
            redisTemplate.opsForStream().add(message);
        } else {
            log.error(message.getValue().get("record"));
            userIntegralAddFailureDao.insert(message.getValue().get("record"));
        }
    }
}
