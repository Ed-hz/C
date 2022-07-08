package com.secure.practice.mapper;

import com.secure.practice.entity.RealName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RealNameMapperTests {
    @Autowired
    RealNameMapper realNameMapper;
    @Test
    public void insert()
    {
        RealName realName = new RealName();
        realName.setUid(1);
        realName.setUsername("2");
        realName.setName("xia");
        realName.setId("12345");
        realName.setState(1);
        realNameMapper.insert(realName);
    }

    @Test
    public void count()
    {
        System.out.println(realNameMapper.selectCount(null));
    }
}
