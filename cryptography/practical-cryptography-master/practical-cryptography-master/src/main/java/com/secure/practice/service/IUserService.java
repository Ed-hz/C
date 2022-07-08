package com.secure.practice.service;

import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;


public interface IUserService {
    /**
     * 用户注册方法
     * @param user 用户的数据对象
     */
    void reg(User user);

    /**
     * 用户登陆功能
     * @param username 用户名
     * @param password 用户密码
     * @return 当前匹配的用户数据，如果没有匹配到则返回null
     */
    User login(String username, String password);

    /**
     * 更新用户信息
     * @param user 用户
     * @return 是否更新成功
     */
    boolean update(User user);

    /**
     * 获取所有的用户（处于注销或者未注销状态）
     * @param start 开始位置
     * @param count 返回数量
     * @return Page类型，包含总数，和该页的用户列表
     */
    Page<User> getAllUsers(Integer start, Integer count);

    /**
     * 通过uid查询用户（处于注销或者未注销状态）
     * @param uid 用户uid
     * @return 用户
     */
    User findUserByUid(Integer uid);

    /**
     * 给用户使用，屏蔽一些用户不可见信息，通过用户uid查询用户，用于用户更新自身信息时查询
     * @param uid 用户uid
     * @return 用户
     */
    User findUserByUidForUser(Integer uid);

    /**
     * 用户修改密码
     * @param uid 用户uid
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean modifyPass(Integer uid,String oldPassword,String newPassword);

    /**
     * 管理员强制修改用户密码
     * @param uid 要修改的用户uid
     * @param newPassword 新密码
     * @param su 管理员
     * @return
     */
    boolean modifyPass(Integer uid,String newPassword,Su su);

}
