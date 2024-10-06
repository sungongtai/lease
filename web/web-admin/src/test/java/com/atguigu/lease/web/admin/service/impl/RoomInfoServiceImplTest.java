package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RoomInfoServiceImplTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void test(){
        redisTemplate.delete(RedisConstant.APP_ROOM_PREFIX + 3);
    }

}