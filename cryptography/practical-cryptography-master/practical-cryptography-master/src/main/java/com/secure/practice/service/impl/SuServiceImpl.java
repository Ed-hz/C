package com.secure.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.mapper.SuMapper;
import com.secure.practice.mapper.UserMapper;
import com.secure.practice.service.ISuService;
import com.secure.practice.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class SuServiceImpl implements ISuService {
    @Autowired
    private SuMapper suMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public User searchUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User result = userMapper.selectOne(queryWrapper);
        return result;
    }

    @Override
    public boolean deleteUser(String username,Su su) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User result = userMapper.selectOne(queryWrapper);
        if (result==null)
        {
            throw new UsernameNotFoundException();
        }
        result.setIsDelete(1);
        result.setModifiedUser("su_"+su.getUsername());
        result.setModifiedTime(new Date());
        return userMapper.updateById(result)==1;
    }

    @Override
    public boolean recoverUser(String username, Su su) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User result = userMapper.selectOne(queryWrapper);
        if (result==null)
        {
            throw new UsernameNotFoundException();
        }
        result.setIsDelete(0);
        result.setModifiedUser("su_"+su.getUsername());
        result.setModifiedTime(new Date());
        return userMapper.updateById(result)==1;
    }

    @Override
    public boolean updateUser(Su su, User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",user.getUid());
        User byUid = userMapper.selectOne(queryWrapper);
        if (byUid.getUsername().equals(user.getUsername()))
        {
            byUid.setGender(user.getGender());
            byUid.setEmail(user.getEmail());
            byUid.setPhone(user.getPhone());
            byUid.setAvatar(user.getAvatar());
            byUid.setUtype(user.getUtype());
            byUid.setModifiedTime(new Date());
            byUid.setModifiedUser("su_"+su.getUsername());
            return userMapper.updateById(byUid)==1;
        }
        return false;
    }
    @Override
    public void reg(Su user) {
//        //检查用户名命名是否符合规则
//        if (user.getUsername().length()<5)
//        {
//            throw new IllegalUsernameException("用户名不合法，用户名必须大于5个字符");
//        }
//        else if (user.getUsername().substring(0,3).equals("su_"))
//        {
//            throw new IllegalUsernameException("用户名不合法，不允许su_开头");
//        }

        String username = user.getUsername();
        QueryWrapper<Su> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Su result = suMapper.selectOne(queryWrapper);
        if (result!=null)
        {
            throw new UsernameDuplicatedException("用户名被占用");
        }

        //加密处理
        //串 + password + 串 --- md5加密，连续加载三次
        //盐值 + password + 盐值
        String oldPassword = user.getPassword();
        //获取盐值（随机生成一个盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMD5Password(oldPassword,salt);
        user.setPassword(md5Password);
        user.setSalt(salt);

        //补全数据
        user.setIsDelete(0);
        user.setCreatedUser("su_"+user.getUsername());
        user.setModifiedUser("su_"+user.getUsername());
        Date date=new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        //判断是否插入成功
        Integer rows = suMapper.insert(user);
        if (rows!=1)
        {
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }

    @Override
    public Su login(String username, String password) {
        QueryWrapper<Su> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Su result = suMapper.selectOne(queryWrapper);
        if(result==null){
            throw new UsernameNotFoundException("用户数据不存在");
        }
        if (!getMD5Password(password,result.getSalt()).equals(result.getPassword()))
        {
            throw new PasswordNotMatchException("用户密码错误");
        }
        //查看是否被删除
        if (result.getIsDelete()==1)
        {
            throw new UsernameNotFoundException("用户数据不存在");
        }
        //减少层与层之间的传输数据量，提升性能
        Su user=new Su();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setGender(result.getGender());
        user.setAvatar(result.getAvatar());

        return user;
    }

    @Override
    public void update(Su user) {
        user.setModifiedTime(new Date());
        user.setModifiedUser("su_"+user.getUsername());
        Integer update = suMapper.updateById(user);
        if (update==0)
        {
            throw new UsernameNotFoundException();
        }
        else if (update!=1)
        {
            throw new UpdateException();
        }
    }

    @Override
    public Su getUserByUid(Integer uid) {
        QueryWrapper<Su> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        return suMapper.selectOne(queryWrapper);
    }

    /** 定义一个md5算法的加密处理*/
    private String getMD5Password(String password,String salt){
        //md5加密算法方法的调用
        for (int i = 0;i<1;i++ )
        {
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        return password;

    }

}
