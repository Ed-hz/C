package com.secure.practice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;
import com.secure.practice.mapper.CertificateMapper;
import com.secure.practice.service.ICertificateService;
import com.secure.practice.service.ex.CaStateException;
import com.secure.practice.service.ex.CaTypeException;
import com.secure.practice.service.ex.UserTypeException;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.AESForSQL;
import com.secure.practice.utils.RSA;
import com.secure.practice.utils.ca.CertUtil;
import com.secure.practice.utils.ca.DigitalCertificateGenerator;
import com.secure.practice.utils.ca.SignedCertInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.security.PrivateKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements ICertificateService {
    @Autowired
    private CertificateMapper certificateMapper;
    String path = "CA/";
    String CAKeystore = path+"CA.keystore";
    String rootCer = path+"root.cer";
    String personalCer = path + "personal.cer";
    String commercialCer = path +"commercial.cer";
    String personalPath = "personal/";
    String commercialPath = "commercial/";
    String CAKeystorePass = "CA92666";
    @Override
    public Integer add(User opUser, User ownUser,Certificate certificate) {
        //设定证书别名（ID）
        //设定证书类型，是商用还是个人，到时候使用不同的证书来签名
        Integer utype = ownUser.getUtype();
        Certificate result =  new Certificate();
//        result.setAlias(alias);
        if (utype==null)
        {
            throw new UserTypeException("用户类型异常，类型为"+utype);
        }
        else{
            Integer type = certificate.getType();
            switch (utype)
            {
                case 1:

                    switch (type){
                        case 1:
                            result.setType(1);
                            break;
                        case 2:
                            throw new CaTypeException("当前用户为个人版，无法申请商业版数字证书");
                        default:
                            throw new CaTypeException("证书类型异常，类型为"+type);
                    }
                    break;
                case 2:
                    switch (type){
                        case 1:
                            result.setType(1);
                            break;
                        case 2:
                            result.setType(2);
                            break;
                        default:
                            throw new CaTypeException("证书类型异常，类型为"+type);
                    }
                    break;
                default:
                    throw new UserTypeException("用户类型异常，类型为"+utype);
            }
        }
        result.setUid(ownUser.getUid());
        result.setState(0);
        result.setValidTime(certificate.getValidTime());
        result.setStartTime(certificate.getStartTime());
        result.setUsername(ownUser.getUsername());
        result.setDescription(certificate.getDescription());
        result.setCreatedTime(new Date());
        result.setCreatedUser(opUser.getUsername());
        result.setModifiedUser(opUser.getUsername());
        result.setModifiedTime(new Date());
        result.setC(certificate.getC());
        result.setSt(certificate.getSt());
        result.setCn(certificate.getCn());
        result.setL(certificate.getL());
        result.setO(certificate.getO());
        result.setOu(certificate.getOu());
        return certificateMapper.insert(result);
    }

    @Override
    public Integer add(Su opUser, User ownUser, Certificate certificate) {
        User user = new User();
        user.setUsername("su_"+opUser.getUsername());
        return add(user,ownUser,certificate);
    }
    @Override
    public boolean active(Integer cid,Su opUser)
    {
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",0);
        queryWrapper.eq("cid",cid);
        Certificate byCid = certificateMapper.selectOne(queryWrapper);
        if (byCid==null)
            return false;
//        Certificate byCid = certificateMapper.findByCid(cid);
        return sign(byCid,opUser);
    }
    @Override
    public boolean sign(Certificate certificate,Su opUser)
    {
        String alias;
        Certificate temp = new Certificate();
        //循环找到不重名的id
        do {
            alias= UUID.randomUUID().toString().replace("-","");
            QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("alias", AESForSQL.encrypt(alias));
            temp = certificateMapper.selectOne(queryWrapper);
        }while (temp!=null);
        certificate.setAlias(alias);
        String password = UUID.randomUUID().toString().replace("-","");
        certificate.setPassword(password);
        try {
            SignedCertInfo signedCertInfo = new SignedCertInfo();
            signedCertInfo.setC(certificate.getC());
            signedCertInfo.setCN(certificate.getCn());
            signedCertInfo.setL(certificate.getL());
            signedCertInfo.setO(certificate.getO());
            signedCertInfo.setOU(certificate.getOu());
            signedCertInfo.setST(certificate.getSt());
            signedCertInfo.setSubjectAlias(certificate.getAlias());
            signedCertInfo.setSubjectAliasPass(certificate.getPassword());
            signedCertInfo.setKeyStorePass("CA92666");// 颁发者的所在证书库
            signedCertInfo.setKeyStorePath(CAKeystore);// 颁发者证书库路径
            switch (certificate.getType())
            {
                //个人版
                case 1:
                    signedCertInfo.setIssuerAlias("personal");
                    signedCertInfo.setIssuerAliasPass("personal92666");
                    signedCertInfo.setSubjectPath(path +personalPath+ certificate.getAlias() + ".cer");// 存储签发证书的路径
                    break;
                case 2:
                    signedCertInfo.setIssuerAlias("commercial");
                    signedCertInfo.setIssuerAliasPass("commercial92666");
                    signedCertInfo.setSubjectPath(path +commercialPath+ certificate.getAlias() + ".cer");// 存储签发证书的路径
                    break;
                default:
                    throw new CaTypeException("CA证书类型异常，为"+certificate.getType());
            }

            signedCertInfo.setValidity(certificate.getValidTime());// 有效期,单位:天
            System.out.println(signedCertInfo);
            // 签发证书("测试证书"的证书),并且存储到证书库("CurrentTest.keystore")
            DigitalCertificateGenerator.signCertJKSForSubject(signedCertInfo);
            certificate.setId(CertUtil.getSerialNumber(signedCertInfo.getSubjectPath()));
            certificate.setModifiedUser(opUser.getUsername());
            certificate.setModifiedTime(new Date());
            certificate.setState(1);
            certificateMapper.updateById(certificate);
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    @Override
    public List<Certificate> findByUser(User ownUser) {
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",ownUser.getUid());
        return certificateMapper.selectList(queryWrapper);
//        return certificateMapper.findByUid(ownUser.getUid());
    }

    @Override
    public List<Certificate> findByUser(User ownUser, Integer state) {
//        WHERE uid = #{uid}
//        and c_state = #{state}
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",ownUser.getUid());
        queryWrapper.eq("state",state);
        return certificateMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Certificate> findCaByUidForUser(Integer uid, Integer start, Integer count) {
        Page<Certificate> page = new Page<>();
//        WHERE uid = #{uid} and c_state != 0 and c_state != 2 LIMIT #{start}, #{count}
        QueryWrapper<Certificate> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.ne("state",0);
        queryWrapper.ne("state",2);
        queryWrapper.last("limit "+start+","+count);
//        page.setList(certificateMapper.findCaByUidForUser(uid,start,count));
        page.setList(certificateMapper.selectList(queryWrapper));
        page.setTotal(certificateMapper.countCaByUidForUser(uid));
        return page;
    }

    @Override
    public Page<Certificate> findApplyByUidForUser(Integer uid, Integer start, Integer count) {

        Page<Certificate> page = new Page<>();
//        WHERE uid = #{uid} and c_state != 1 and c_state != 3 LIMIT #{start}, #{count}
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        queryWrapper.ne("state",1);
        queryWrapper.ne("state",3);
        queryWrapper.last("limit "+start+","+count);
        page.setList(certificateMapper.selectList(queryWrapper));
        page.setTotal(certificateMapper.countApplyByUidForUser(uid));
        return page;
    }

    @Override
    public String getPriKey(String ID,Integer uid,String key) throws Exception {

//        WHERE id = #{id} and c_state != 2  and c_state != 0
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",ID);
        queryWrapper.ne("state",2);
        queryWrapper.ne("state",0);
        Certificate caByID = certificateMapper.selectOne(queryWrapper);
//        Certificate caByID = certificateMapper.findCaByID(ID);
        if (caByID!=null&&caByID.getUid().equals(uid))
        {
            //派生私钥
            String result = DigestUtils.md5DigestAsHex((key).getBytes()).toUpperCase();
            System.out.println("md5变换后生成的加密私钥的私钥"+result);

            //测试服务器发送AES加密过的私钥，用户
            PrivateKey privateKey = CertUtil.privateKeyInJKS(CAKeystore, CAKeystorePass, caByID.getAlias(), caByID.getPassword());
            byte[] priKey = privateKey.getEncoded();
            String priKeyStr = RSA.encryptBASE64(priKey);
            System.out.println("从证书库中获取到私钥为\n"+priKeyStr);
            String priKeyStrC= AES.encrypt(priKeyStr,result);
            System.out.println("返回给用户加密过的私钥为\n"+priKeyStrC);
            return priKeyStrC;
        }
        return null;
    }

    @Override
    public List<Certificate> findCaByUsername(String username) {
//        username = #{username} and c_state != 0 and c_state != 2;
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.ne("state",0);
        queryWrapper.ne("state",2);
        return certificateMapper.selectList(queryWrapper);
//        return certificateMapper.findCaByUsername(username);
    }

    @Override
    public List<Certificate> getCaRecalled() {
//        WHERE c_state = 3
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",3);
        List<Certificate> certificates = certificateMapper.selectList(queryWrapper);
        for (Certificate certificate:certificates)
            certificate.ignoreMore();
        return certificates;
//        return certificateMapper.caRecalled();
    }

    @Override
    public List<Certificate> findApplyByUsername(String username) {
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",0);
        queryWrapper.eq("username",username);
        return certificateMapper.selectList(queryWrapper);
//        return certificateMapper.findApplyByUsername(username);
    }
    @Override
    public Page<Certificate> findApply(Integer start, Integer count)
    {
        Page<Certificate> page = new Page<>();

//        WHERE c_state = 0
//        LIMIT #{start}
//            , #{count}

        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",0);
        queryWrapper.last("limit "+start+","+count);
//        page.setList(certificateMapper.findApply(start,count));
        page.setList(certificateMapper.selectList(queryWrapper));
        page.setTotal(certificateMapper.countApply());
        return page;
    }
    @Override
    public Page<Certificate> findAfterDeal(Integer start, Integer count){
        Page<Certificate> page = new Page<>();
//        WHERE c_state != 0 and c_state !=2
//        LIMIT #{start}
//            , #{count}
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("state",0);
        queryWrapper.ne("state",2);
        queryWrapper.last("limit "+start+","+count);
//        page.setList(certificateMapper.findCa(start,count));
        page.setList(certificateMapper.selectList(queryWrapper));
        page.setTotal(certificateMapper.countCa());
        return page;
    }

//    @Override
//    public List<Certificate> findCaByAlias(String alias) {
//        List<Certificate> list = new ArrayList<>();
//        list.add(certificateMapper.findByAlias(alias));
//        return list;
//    }

    @Override
    public boolean update(User opUser,Certificate certificate){
        return update(opUser.getUsername(),certificate);
    }
    @Override
    public boolean update(Su opUser,Certificate certificate){
        return update("su_"+opUser.getUsername(),certificate);
    }


    private boolean update(String username,Certificate certificate){
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",certificate.getCid());
        Certificate newCertificate = certificateMapper.selectOne(queryWrapper);
        if (newCertificate == null)
            return false;
//        Certificate newCertificate = certificateMapper.findByCid(certificate.getCid());
        if (newCertificate.getState().equals(0)||newCertificate.getState().equals(2)) {
            newCertificate.setModifiedUser(username);
            newCertificate.setModifiedTime(new Date());
            Integer type = certificate.getType();
            if (certificate.getState().equals(0)) {
                switch (type) {
                    case 1:
                    case 2:
                        newCertificate.setType(type);
                        break;
                    default:
                        throw new CaTypeException("更新时，证书类型异常！");

                }
            }
            newCertificate.setState(0);
            newCertificate.setSt(certificate.getSt());
            newCertificate.setDescription(certificate.getDescription());
            newCertificate.setStartTime(certificate.getStartTime());
            newCertificate.setValidTime(certificate.getValidTime());
            newCertificate.setC(certificate.getC());
            newCertificate.setCn(certificate.getCn());
            newCertificate.setL(certificate.getL());
            newCertificate.setO(certificate.getO());
            newCertificate.setOu(certificate.getOu());
            return certificateMapper.updateById(newCertificate) == 1;
        }
        else return false;
    }
    @Override
    public boolean refuse(Su opUser, Integer cid) {
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",0);
        queryWrapper.eq("cid",cid);
        Certificate certificate = certificateMapper.selectOne(queryWrapper);
        if (certificate == null)
            throw new CaStateException("证书状态异常，驳回失败，证书cid"+cid+"su:"+opUser.getUid());
//        Certificate certificate = certificateMapper.findByCid(cid);
        certificate.setState(2);
        return certificateMapper.updateById(certificate)==1;
    }

    @Override
    public boolean recall(User opUser, String id) {
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Certificate byId = certificateMapper.selectOne(queryWrapper);
        if (byId.getState().equals(1))
        {
            byId.setModifiedUser(opUser.getUsername());
            byId.setModifiedTime(new Date());
            byId.setState(3);
            return certificateMapper.updateById(byId)==1;
        }
        else {
            throw new CaStateException("证书状态异常，无法召回");
        }

    }

    @Override
    public boolean recall(Su opUser, String id) {
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Certificate byId = certificateMapper.selectOne(queryWrapper);
//        Certificate byAlias = certificateMapper.findByAlias(alias);
        if (byId.getState().equals(1))
        {
            byId.setModifiedUser("su_"+opUser.getUsername());
            byId.setModifiedTime(new Date());
            byId.setState(3);
            return certificateMapper.updateById(byId)==1;
        }
        else {
            throw new CaStateException("证书状态异常，无法召回");
        }
    }

    @Override
    public boolean recallRevocation(Su opUser, String id) {
        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Certificate byId = certificateMapper.selectOne(queryWrapper);
//        Certificate byAlias = certificateMapper.findByAlias(alias);
        if (byId.getState().equals(3))
        {
            byId.setModifiedUser("su_"+opUser.getUsername());
            byId.setModifiedTime(new Date());
            byId.setState(1);
            return certificateMapper.updateById(byId)==1;
        }
        else {
            throw new CaStateException("证书状态异常，无法召回");
        }
    }

    @Override
    public Certificate findCaByID(String id) {
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        queryWrapper.ne("state",2);
        queryWrapper.ne("state",0);
        return certificateMapper.selectOne(queryWrapper);
//        return certificateMapper.findCaByID(id);
    }

    @Override
    public Certificate findCaByIDAndUid(String id,Integer uid) {

//        WHERE id = #{id} and uid=#{uid} and c_state != 2  and c_state != 0
        QueryWrapper<Certificate> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("id",id);
        queryWrapper.eq("uid",uid);
        queryWrapper.ne("state",2);
        queryWrapper.ne("state",0);
        Certificate certificate = certificateMapper.selectOne(queryWrapper);
        certificate.ignore();
        return certificate;
//        return certificateMapper.findCaByIDAndUid(id,uid);
    }
}
