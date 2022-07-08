package com.secure.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.RealName;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;
import com.secure.practice.mapper.RealNameMapper;
import com.secure.practice.mapper.UserMapper;
import com.secure.practice.service.IRealNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealNameService implements IRealNameService {
    @Autowired
    RealNameMapper realNameMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public RealName getRealNameInfo(Integer uid) {
        QueryWrapper<RealName> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        RealName realName = realNameMapper.selectOne(queryWrapper);
        if (realName == null)
        {
            realName = new RealName();
            realName.setName("");
            realName.setState(0);
            realName.setId("");
        }
        realName.setUid(null);
        realName.setUsername(null);
        realName.setRid(null);
        return realName;
    }

    @Override
    public void CommitRealNameInfo(Integer uid, String name, String id) {
        QueryWrapper<RealName> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        RealName realName = realNameMapper.selectOne(queryWrapper);
        QueryWrapper<User> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("uid",uid);
        User user = userMapper.selectOne(queryWrapper1);
        if (realName == null)
        {
            realName = new RealName();
            realName.setUid(uid);
            realName.setUsername(user.getUsername());
            //由于没有国家专用实名接口，这里直接通过
            realName.setState(2);
            if (user.getUtype()==null||user.getUtype().equals(0))
                user.setUtype(1);
            realName.setId(id);
            realName.setName(name);
            realNameMapper.insert(realName);
            userMapper.updateById(user);
        }
        else
        {
            realName.setName(name);
            realName.setId(id);
            //由于没有国家专用实名接口，这里直接通过
            realName.setState(2);
            if (user.getUtype()==null||user.getUtype().equals(0))
                user.setUtype(1);
            realNameMapper.updateById(realName);
            userMapper.updateById(user);
        }
    }

    @Override
    public Page<RealName> getRealNamePage(Integer start, Integer count) {
        Page<RealName> page = new Page<>();
        QueryWrapper<RealName> queryWrapper =new QueryWrapper<>();
        queryWrapper.last("limit "+start+","+count);
        page.setList(realNameMapper.selectList(queryWrapper));
        page.setTotal(Math.toIntExact(realNameMapper.selectCount(null)));
        return page;
    }

    @Override
    public RealName searchRealByUsername(String username) {
        QueryWrapper<RealName> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return realNameMapper.selectOne(queryWrapper);
    }
}
