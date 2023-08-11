package com.junhe.integral.config;

import com.junhe.integral.common.RedisUtil;
import com.junhe.integral.constant.RedisKeys;
import com.junhe.integral.core.integral.dto.IntegralDetailDTO;
import com.junhe.integral.core.integral.listener.UserIntegralDetailListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * redis消息队列配置
 * @author HEJUN
 * @since 1.0
 * @date 2023/7/3
 */
@Component
@Slf4j
public class RedisStreamConfig implements ApplicationRunner {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedisConnectionFactory connectionFactory;
    @Resource
    private UserIntegralDetailListener streamListener;

    @Override
    public void run(ApplicationArguments args) {
        log.info("开始创建redis stream消息队列");
        if (redisUtil.existsKey(RedisKeys.INTEGRAL_STREAM)) {
            try {
                redisUtil.createStreamConsumerGroup(RedisKeys.INTEGRAL_STREAM, RedisKeys.INTEGRAL_ASYNC_PERSISTENCE_GROUP);
            } catch (Exception e) {
                log.error("Error creating consumer group " + RedisKeys.INTEGRAL_ASYNC_PERSISTENCE_GROUP);
                System.out.println("Error creating consumer group " + RedisKeys.INTEGRAL_ASYNC_PERSISTENCE_GROUP);
            }
        }
        log.info("redis stream消息队列创建完毕");

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(connectionFactory,
                containerOptions);

        container.receive(StreamOffset.fromStart(RedisKeys.INTEGRAL_STREAM), streamListener);
        container.start();
    }

}
