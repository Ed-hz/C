package com.secure.practice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.secure.practice.utils.AESEncryptHandler;

import java.util.Date;
@TableName(value = "t_ca", autoResultMap = true)
public class Certificate extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer cid;
    private Integer uid;
    @TableField(typeHandler = AESEncryptHandler.class)
    private String password;
    private Integer type;
    @TableField(typeHandler = AESEncryptHandler.class)
    private String description;
    @TableField("start_time")
    private Date startTime;
    @TableField("valid_time")
    private Integer validTime;
    private Integer state;
    @TableField(typeHandler = AESEncryptHandler.class)
    private String username;
    @TableField(typeHandler = AESEncryptHandler.class)
    private String alias;
    private String c;
    private String cn;
    private String l;
    private String o;
    private String ou;
    private String st;
    private String id;

    public void ignore()
    {
        super.ignore();
        password = null;
        alias = null;
        uid = null;
        username = null;
    }

    public void ignoreMore()
    {
        ignore();
        cid=null;
        description = null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "cid=" + cid +
                ", uid=" + uid +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", validTime=" + validTime +
                ", state=" + state +
                ", username='" + username + '\'' +
                ", alias='" + alias + '\'' +
                ", c='" + c + '\'' +
                ", cn='" + cn + '\'' +
                ", l='" + l + '\'' +
                ", o='" + o + '\'' +
                ", ou='" + ou + '\'' +
                ", st='" + st + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getState() {
        return state;
    }


    public void setState(Integer state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
