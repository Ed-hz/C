package com.secure.practice.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTests {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void setTest()
    {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        stringRedisTemplate.opsForValue().set("name","javaBoy");
//        Assert.assertEquals(ops.get("name"),"javaBoy");
        System.out.println(ops.get("934dfee9-f2ff-48b1-b006-d0f4028b1477"));
    }

}
