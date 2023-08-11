package com.junhe.integral.constant;

import com.sun.istack.internal.NotNull;

/**
 * redis key常量类
 * @author HEJUN
 * @since 1.0
 * @date 2023/6/26
 */
public class RedisKeys {

    public static final String INTEGRAL_STREAM = "INTEGRAL:STREAM";

    public static final String INTEGRAL_ASYNC_PERSISTENCE_GROUP = "INTEGRAL:ASYNC:PERSISTENCE";

    private static final String USER_LOCK_KEY_PREFIX = "USER:LOCK:";

    private static final String USER_INTEGRAL_TOTAL_KEY_PREFIX = "USER:INTEGRAL:TOTAL:";

    private static final String INTEGRAL_EVENT_CACHE = "INTEGRAL:EVENT:CACHE";


    /**
     * 获取redis用户锁KEY值
     * @param userId
     * @return
     */
    public static String getUserLockKey(@NotNull String userId){
        return USER_LOCK_KEY_PREFIX + userId;
    }

    /**
     * 获取用户积分总数缓存KEY值
     * @param userId
     * @return
     */
    public static String getUserIntegralTotalKey(String userId) {
        return USER_INTEGRAL_TOTAL_KEY_PREFIX + userId;
    }

    /**
     * 获取积分事件缓存KEY
     * @return
     */
    public static String getIntegralEventCache() {
        return INTEGRAL_EVENT_CACHE;
    }

    public static void main(String[] args) {
        System.out.println(RedisKeys.getUserLockKey(null));
    }
}
