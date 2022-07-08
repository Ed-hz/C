package com.secure.practice.entity.vo;

import com.secure.practice.entity.User;

public class UserVO extends User {
    private String userToken;
    private String verifyCode;
    private String verifyKey;
    private String priKey;

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public User getUser()
    {
        User user = new User();
        user.setUsername(super.getUsername());
        user.setPassword(super.getPassword());
//        user.setSalt(super.getSalt());
//        user.setUid(super.getUid());
        user.setEmail(super.getEmail());
//        user.setAvatar(super.getAvatar());
        user.setPhone(super.getPhone());
        user.setGender(super.getGender());
        return user;
    }


    @Override
    public String toString() {
        return "UserVO{" +
                "userToken='" + userToken + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", verifyKey='" + verifyKey + '\'' +
                '}';
    }
}
