package com.secure.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;
import com.secure.practice.mapper.UserMapper;
import com.secure.practice.service.IUserService;
import com.secure.practice.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void reg(User user) {
        //检查用户名命名是否符合规则
        if (user.getUsername().length()<5)
        {
            throw new IllegalUsernameException("用户名不合法，用户名必须大于5个字符");
        }
        else if (user.getUsername().substring(0,3).equals("su_"))
        {
            throw new IllegalUsernameException("用户名不合法，不允许su_开头");
        }

        String username = user.getUsername();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("is_delete",0);
        User result = userMapper.selectOne(queryWrapper);
        if (result!=null)
        {
            throw new UsernameDuplicatedException("用户名被占用");
        }

        //加密处理
        //串 + password + 串 --- md5加密
        //盐值 + password + 盐值
        String oldPassword = user.getPassword();
        //获取盐值（随机生成一个盐值）
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMD5Password(oldPassword,salt);
        user.setPassword(md5Password);
        user.setSalt(salt);
        if (user.getAvatar()==null)
            user.setAvatar("");
        //补全数据
        user.setIsDelete(0);
        user.setUtype(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date=new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);
        //判断是否插入成功
        Integer rows = userMapper.insert(user);
        if (rows!=1)
        {
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).eq("is_delete",0);
        User result = userMapper.selectOne(queryWrapper);
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
        User user=new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setGender(result.getGender());
        user.setAvatar(result.getAvatar());
        user.setUtype(result.getUtype());
        return user;
    }

    @Override
    public boolean update(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",user.getUid());
        User byUid = userMapper.selectOne(queryWrapper);
        if (byUid.getUsername().equals(user.getUsername()))
        {
            byUid.setGender(user.getGender());
            byUid.setEmail(user.getEmail());
            byUid.setPhone(user.getPhone());
            byUid.setAvatar(user.getAvatar());
            byUid.setModifiedTime(new Date());
            byUid.setModifiedUser(user.getUsername());
            return userMapper.updateById(byUid)==1;
        }
        return false;
    }
    @Override
    public Page<User> getAllUsers(Integer start, Integer count) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit "+start+","+count);
        List<User> result = userMapper.selectList(queryWrapper);
//        List<User> result = userMapper.getAllList(start,count);
        if (result==null)
            throw new SelectException("选择异常");
        else
        {
            Page<User> page = new Page<>();
            page.setTotal(userMapper.count());
            page.setList(result);
            return page;
        }

    }

    @Override
    public User findUserByUid(Integer uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        return userMapper.selectOne(queryWrapper);
    }
    @Override
    public User findUserByUidForUser(Integer uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        User user = userMapper.selectOne(queryWrapper);
        user.ignore();
        return user;
    }

    @Override
    public boolean modifyPass(Integer uid, String oldPassword, String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        User byUid = userMapper.selectOne(queryWrapper);
        if (byUid!=null)
        {
            if (getMD5Password(oldPassword,byUid.getSalt()).equals(byUid.getPassword()))
            {
                String salt = UUID.randomUUID().toString().toUpperCase();
                String md5Password = getMD5Password(newPassword,salt);
                byUid.setPassword(md5Password);
                byUid.setSalt(salt);
                byUid.setModifiedTime(new Date());
                byUid.setModifiedUser(byUid.getUsername());
                return userMapper.updateById(byUid)==1;
            }
            else
                return false;
        }
        else
            return false;
    }

    @Override
    public boolean modifyPass(Integer uid, String newPassword, Su su) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        User byUid = userMapper.selectOne(queryWrapper);
        if (byUid!=null)
        {
            String salt = UUID.randomUUID().toString().toUpperCase();
            String md5Password = getMD5Password(newPassword,salt);
            byUid.setPassword(md5Password);
            byUid.setSalt(salt);
            byUid.setModifiedUser("su_"+su.getUsername());
            byUid.setModifiedTime(new Date());
            if (getMD5Password(newPassword,byUid.getSalt()).equals(byUid.getPassword()))
            {
                return userMapper.updateById(byUid)==1;
            }
            return false;
        }
        else
            return false;
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
