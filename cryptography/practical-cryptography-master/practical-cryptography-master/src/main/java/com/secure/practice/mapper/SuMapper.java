package com.secure.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;

import java.util.List;

public interface SuMapper extends BaseMapper<Su>{
    /**
     * 插入用户的数据
     * @param su 用户数据
     * @return 受影响的行数（增删改都会影响行数，据此判断是否执行成功）
     */
    Integer insertSu(Su su);

    /**
     * 根据用户名查询用户的数据
     * @param username 用户名
     * @return 如果找到对应的用户，就返回这个用户的数据，如果没有返回null
     */
    Su findByUsername(String username);

    /**
     * 根据用户名查询用户的数据
     * @param uid 用户名
     * @return 如果找到对应的用户，就返回这个用户的数据，如果没有返回null
     */
    Su findByUid(Integer uid);

    /**
     * 返回从start开始的count个数量的用户
     * @param start 开始位置
     * @param count 返回的数量
     * @return 查询结果
     */
    List<User> getList(Integer start, Integer count);

    /**
     * 更新用户信息
     * @param su 管理员用户
     * @return
     */
    Integer updateSu(Su su);
}
