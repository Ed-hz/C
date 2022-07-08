package com.secure.practice.service;

import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;

public interface ISuService {

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 查询结果
     */
    User searchUser(String username);

    /**
     * 以管理员身份删除某个用户
     * @param username 要删除的用户
     * @param
     */
    boolean deleteUser(String username, Su su);

    /**
     * 使用管理员身份恢复用户注销状态
     * @param username 用户名
     * @param su 管理员
     * @return
     */
    boolean recoverUser(String username, Su su);
    /**
     * 用户登陆功能
     * @param username 用户名
     * @param password 用户密码
     * @return 当前匹配的用户数据，如果没有匹配到则返回null
     */
    Su login(String username, String password);

    /**
     * 更新管理员信息
     * @param user 管理员信息
     */
    void update(Su user);

    /**
     * 通过管理员的uid获取管理员
     * @param uid 管理员uid
     * @return 管理员
     */
    Su getUserByUid(Integer uid);

    /**
     * 使用管理员身份更新用户信息
     * @param su 管理员
     * @param user 新用户信息
     * @return
     */
    boolean updateUser(Su su, User user);

    /**
     * 管理员注册
     * @param user 管理员
     */
    void reg(Su user);
}
