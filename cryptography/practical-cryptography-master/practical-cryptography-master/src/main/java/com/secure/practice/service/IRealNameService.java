package com.secure.practice.service;

import com.secure.practice.entity.RealName;
import com.secure.practice.entity.vo.Page;

public interface IRealNameService {
    /**
     * 用户获取自身实名信息
     * @param uid 用户uid
     * @return 屏蔽细节后的实名信息
     */
    RealName getRealNameInfo(Integer uid);

    /**
     * 用户提交自身信息，由于没有接入国家相关数据库，这里提交后默认通过认证
     * @param uid 用户uid
     * @param name 真实姓名
     * @param id 身份证号
     */
    void CommitRealNameInfo(Integer uid,String name,String id);


    /**
     * 管理员查看实名认证信息
     * @param start 开始位置
     * @param count 返回数量
     * @return
     */
    Page<RealName> getRealNamePage(Integer start,Integer count);

    /**
     * 管理员使用
     * @param username 用户名
     * @return
     */
    RealName searchRealByUsername(String username);
}
