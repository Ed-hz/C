package com.secure.practice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.User;

import java.util.List;

public interface CertificateMapper extends BaseMapper<Certificate>{
    /**
     * 插入证书
     * @param certificate 证书
     * @return 受影响的行数（增删改都会影响行数，据此判断是否执行成功）
     */
    Integer insertCa(Certificate certificate);

    /**
     * 返回用户名下所有证书（处于申请，驳回，生效，撤回状态）
     * @param uid 用户uid
     * @return 用户名下所有的证书
     */
    List<Certificate> findByUid(Integer uid);

    /**
     * 通过用户名查找名下所有申请状态的证书
     * @param username 用户名
     * @return 证书列表
     */
    List<Certificate> findApplyByUsername(String username);

    /**
     * 通过用户名查找名下所有处于撤回或者生效状态的证书
     * @param username 用户名
     * @return 证书列表
     */
    List<Certificate> findCaByUsername(String username);

    /**
     * 通过证书id查询处于撤回或者生效状态的证书
     * @param id 证书id
     * @return 证书
     */
    Certificate findCaByID(String id);

    /**
     * 查找uid用户名下的为id的证书（处于撤回或者生效状态），给用户使用，屏蔽一些用户不可见的值
     * @param id 证书id
     * @param uid 用户uid
     * @return 证书
     */
    Certificate findCaByIDAndUid(String id,Integer uid);

    /**
     * 通过uid查询名下处于撤回或者生效状态的证书，给用户使用，屏蔽一些用户不可见的值
     * @param uid 用户uid
     * @param start 开始位置
     * @param count 返回数量
     * @return 证书列表
     */
    List<Certificate> findCaByUidForUser(Integer uid, Integer start,Integer count);

    /**
     * 通过uid查询名下处于撤回或者生效状态的证书数量
     * @param uid 用户uid
     * @return 符合条件的证书数量
     */
    Integer countCaByUidForUser(Integer uid);

    /**
     * 通过uid查询名下处于申请或者驳回状态的证书，给用户使用，屏蔽一些用户不可见的值
     * @param uid 用户uid
     * @param start 开始位置
     * @param count 返回数量
     * @return 证书列表
     */
    List<Certificate> findApplyByUidForUser(Integer uid, Integer start,Integer count);

    /**
     * 通过uid查询名下处于申请或者驳回状态的证书数量
     * @param uid 用户uid
     * @return 符合条件的证书数量
     */
    Integer countApplyByUidForUser(Integer uid);

    /**
     * 查询所有处于撤回状态的证书，用于公示，屏蔽一些不可见值
     * @return 撤回状态证书列表
     */
    List<Certificate> caRecalled();

    /**
     * 返回用户名下证书中状态为state的证书
     * @param uid 用户uid
     * @param state 证书状态
     * @return 该状态证书列表
     */
    List<Certificate> findByUidAndState(Integer uid,Integer state);

    /**
     * 管理员使用，返回处于申请或者驳回状态的证书
     * @param start 开始位置
     * @param count 返回数量
     * @return 证书列表
     */
    List<Certificate> findApply(Integer start,Integer count);

    /**
     * 返回处于申请或者驳回状态的证书
     * @return
     */
    Integer countApply();

    /**
     * 管理员使用，返回处于生效或者撤回状态的证书列表
     * @param start 开始位置
     * @param count 返回数量
     * @return 证书列表
     */
    List<Certificate> findCa(Integer start,Integer count);

    /**
     * 返回处于生效或者撤回状态的证书数量
     * @return 证书数量
     */
    Integer countCa();

    /**
     * 通过cid查找证书（处于申请。驳回，生效，撤回状态）
     * @param cid 证书cid
     * @return 证书
     */
    Certificate findByCid(Integer cid);

    /**
     * 通过证书别名查找证书（处于申请。驳回，生效，撤回状态）
     * @param alias 证书别名
     * @return 证书
     */
    Certificate findByAlias(String alias);

    /**
     * 更新证书
     * @param certificate 新证书信息
     * @return 受影响条目数量
     */
    Integer updateCa(Certificate certificate);


}
