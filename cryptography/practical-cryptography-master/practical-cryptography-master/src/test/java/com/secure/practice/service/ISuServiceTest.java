package com.secure.practice.service;

import com.secure.practice.entity.Su;
import com.secure.practice.mapper.SuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ISuServiceTest {
    @Autowired
    ISuService suService;
    @Autowired
    SuMapper suMapper;
    @Test
    public void reg(){
        Su su =new Su();
        su.setUsername("root");
        su.setPassword("root92666");
        su.setPhone("15683141454");
        su.setEmail("1907058805@qq.com");
        su.setGender(1);
        suMapper.insert(su);
    }
}
