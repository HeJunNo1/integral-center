package com.junhe.integral.listener;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 积分异步入库监听器
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/30
 */
@Component
public class IntegralAddListener implements StreamListener<String, MapRecord<String, String, Object>> {

    @Override
    public void onMessage(MapRecord<String, String, Object> message) {

        System.out.println("MessageId: " + message.getId());
        System.out.println("Stream: " + message.getStream());
        System.out.println("Body: " + message.getValue());
    }
}


