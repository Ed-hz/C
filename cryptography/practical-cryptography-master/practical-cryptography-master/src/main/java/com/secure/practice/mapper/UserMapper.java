package com.secure.practice.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.secure.practice.entity.User;

import java.util.List;
@TableName(value = "t_user",autoResultMap = true)
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询数据库中所有用户的条数，包括标记注销和未注销
     * @return 条数
     */
    Integer count();

}
