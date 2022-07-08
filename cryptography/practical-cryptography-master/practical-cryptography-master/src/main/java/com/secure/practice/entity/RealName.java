package com.secure.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.secure.practice.utils.AESEncryptHandler;

@TableName(value = "t_real", autoResultMap = true)

public class RealName {
    @TableId(type = IdType.AUTO)
    Integer rid;
    Integer uid;
    String username;
    @TableField(typeHandler = AESEncryptHandler.class)
    String name;
    @TableField(typeHandler = AESEncryptHandler.class)
    String id;
    Integer state;

    @Override
    public String toString() {
        return "RealName{" +
                "rid=" + rid +
                ", uid=" + uid +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", state=" + state +
                '}';
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
