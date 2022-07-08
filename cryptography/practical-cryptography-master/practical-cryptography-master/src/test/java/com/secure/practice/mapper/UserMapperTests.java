package com.secure.practice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.User;
import com.secure.practice.utils.AESForSQL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;
//    @Test
//    public void insert()
//    {
//        User user=new User();
//        user.setUsername("tim2333");
//        user.setPassword("123");
//        Integer rows=userMapper.insert(user);
//        System.out.println(rows);
//    }
//    @Test
//    public void findByUsername()
//    {
//        User user = userMapper.findByUsername("tim");
//        System.out.println(user);
//    }
//    @Test
//    public void getList()
//    {
//        List<User> users = userMapper.getList(1,5);
//        System.out.println(users);
//    }
//    @Test
//    public void update()
//    {
//        User user = userMapper.findByUsername("test1");
//        user.setIsDelete(1);
//        Integer update = userMapper.update(user);
//        System.out.println(update);
//
//    }
    @Test
    public void en()
    {
        List<User> users = userMapper.selectList(null);
        for (User user:users)
        {
            if (user.getPhone()!=null)
                user.setPhone(AESForSQL.encrypt(user.getPhone()));
            if (user.getEmail()!=null)
                user.setEmail(AESForSQL.encrypt(user.getEmail()));
            System.out.println(user);
            System.out.println(userMapper.updateById(user));
        }
    }

    @Test
    public void list()
    {
//        List<User> users = userMapper.selectList(null);
//        for (User user:users)
//        {
//            System.out.println(user);
//        }
        Integer start =1;
        Integer count = 3;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit "+start+","+count);
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user:users)
        {
            System.out.println(user);
        }
    }

    @Test
    public void  count()
    {
        System.out.println(userMapper.count());
    }



}
