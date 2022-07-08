package com.secure.practice.service;

import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;

import java.util.List;


public interface ICertificateService {
    /**
     * 以普通用户身份申请新证书，新证书为申请状态
     * @param opUser 操作用户（普通用户）
     * @param ownUser 拥有用户（管理员用户）
     * @param certificate 证书
     * @return 数据库受影响条数
     */
    Integer add(User opUser,User ownUser,Certificate certificate);

    /**
     * 以管理员身份申请新证书，新证书为申请状态
     * @param opUser 操作用户（管理员）
     * @param ownUser 拥有用户（普通用户）
     * @param certificate 证书
     * @return 数据库受影响条数
     */
    Integer add(Su opUser,User ownUser,Certificate certificate);

    /**
     * 通过用户查找其名下的证书列表，由于效率问题已废弃
     * @param ownUser 用户
     * @return 证书列表
     */
    List<Certificate> findByUser(User ownUser);

    /**
     * 通过用户查找其名下的处于state的证书列表，由于效率问题已废弃
     * @param ownUser 用户
     * @param state 状态
     * @return 证书列表
     */
    List<Certificate> findByUser(User ownUser, Integer state);

    /**
     * 使用opUser用户激活证书
     * @param cid 证书cid
     * @param opUser 操作用户
     * @return 是否激活成功
     */
    boolean active(Integer cid, Su opUser);

    /**
     * 更新信息，更新后状态为0
     * @param opUser 操作用户
     * @param certificate 新证书信息
     * @return 是否操作成功
     */
    boolean update(Su opUser,Certificate certificate);

    /**
     * 驳回证书
     * @param opUser 操作用户
     * @param cid 证书id
     * @return 是否驳回成功
     */
    boolean refuse(Su opUser,Integer cid);

    /**
     * 召回证书（普通用户版）
     * @param opUser 操作用户
     * @param id 证书id
     * @return 是否召回成功
     */
    boolean recall(User opUser,String id);

    /**
     * 召回证书（管理员版）
     * @param opUser 操作用户
     * @param id 证书id
     * @return 是否召回成功
     */
    boolean recall(Su opUser,String id);

    /**
     * 撤销召回
     * @param opUser 操作用户
     * @param id 证书id
     * @return 是否撤销成功
     */
    boolean recallRevocation(Su opUser,String id);

    /**
     * 通过ID查找证书
     * @param id 证书id
     * @return 查找结果
     */
    Certificate findCaByID(String id);

    /**
     * 查找uid名下id的证书
     * @param id 证书id
     * @param uid 用户uid
     * @return 查找结果
     */
    Certificate findCaByIDAndUid(String id,Integer uid);

    /**
     * 激活证书
     * @param certificate 证书
     * @param opUser 操作用户
     * @return 是否激活
     */
    boolean sign(Certificate certificate, Su opUser);

    /**
     * 查找用户名下所有CA（处于激活或者撤销状态）
     * @param username 用户名
     * @return 证书列表
     */
    List<Certificate> findCaByUsername(String username);

    /**
     * 获取所有撤销的CA证书，用于撤销列表的生成
     * @return 撤销证书列表
     */
    List<Certificate> getCaRecalled();

    /**
     * 通过用户名查找名下所有处于申请或者驳回状态的证书
     * @param username 用户名
     * @return 证书列表
     */
    List<Certificate> findApplyByUsername(String username);

    /**
     * 查找处于申请或者驳回状态的证书
     * @param start 开始位置
     * @param count 返回数量
     * @return 分页后的证书列表
     */
    Page<Certificate> findApply(Integer start, Integer count);

    /**
     * 查找所有处于生效或者撤销状态的证书
     * @param start 开始位置
     * @param count 返回数量
     * @return 分页后的证书列表
     */
    Page<Certificate> findAfterDeal(Integer start, Integer count);

//    /**
//     * 通过证书别名查找证书（无论处于什么状态）
//     * @param alias 证书别名
//     * @return 证书列表
//     */
//    List<Certificate> findCaByAlias(String alias);

    /**
     * 适用于用户，通过用户uid查询名下所有处于生效或者撤回状态的证书
     * @param uid 用户uid
     * @param start 开始位置
     * @param count 返回数量
     * @return 分页后返回结果
     */
    public Page<Certificate> findCaByUidForUser(Integer uid, Integer start, Integer count);

    /**
     * 适用于用户，通过用户uid查询名下所有处于申请或者驳回状态的证书
     * @param uid 用户uid
     * @param start 开始位置
     * @param count 返回数量
     * @return 分页后返回结果
     */
    Page<Certificate> findApplyByUidForUser(Integer uid, Integer start, Integer count);

    /**
     * 获取当前用户身份下id为id的证书的加密过的私钥
     * @param ID 证书id
     * @param uid 用户uid
     * @param key 加密私钥的密钥
     * @return 使用key加密过的私钥
     */
    String getPriKey(String ID,Integer uid,String key) throws Exception;
    /**
     * 更新信息，更新后状态为0
     * @param opUser 操作用户
     * @param certificate 证书
     * @return 是否更新成功
     */
    public boolean update(User opUser,Certificate certificate);
}
