package com.secure.practice.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.User;
import com.secure.practice.utils.AESForSQL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CertificateMapperTests {
    @Autowired
    private CertificateMapper certificateMapper;
    @Test
    public void insert()
    {
        Certificate certificate = new Certificate();
        certificate.setUid(12);
        certificate.setPassword("hello");
        certificate.setDescription("测试使用");
        certificate.setState(0);
        certificate.setStartTime(new Date());
        certificate.setValidTime(180);
        certificate.setType(1);
        Integer rows=certificateMapper.insert(certificate);
        System.out.println(rows);
    }
//    @Test
//    public void findByUid()
//    {
//
////        List<Certificate> byUid = certificateMapper.findByUid(12);
//        System.out.println(byUid);
//    }
//    @Test
//    public void findByCid()
//    {
//        Certificate byCid = certificateMapper.findByCid(1);
//        System.out.println(byCid);
//    }
    @Test
    public void findByAlias()
    {

        QueryWrapper<Certificate>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("alias","eefdc0dfda7e4778ab2082c2d0cb02d5");
        Certificate byAlias = certificateMapper.selectOne(queryWrapper);
        System.out.println(byAlias);
    }
//    @Test
//    public void update()
//    {
//        Certificate byCid = certificateMapper.findByCid(1);
//        byCid.setState(1);
//        Integer update = certificateMapper.update(byCid);
//        System.out.println(update);
//    }



    @Test
    public void list()
    {
        List<Certificate> certificates = certificateMapper.selectList(null);
        for (Certificate certificate : certificates)
            System.out.println(certificate);
    }

    @Test
    public void findByState()
    {
//        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("state",1);
//        List<Certificate> certificates1 = certificateMapper.selectList(queryWrapper);
//        for (Certificate certificate:certificates1)
//        {
//            System.out.println(certificate);
//        }

//        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne("state",0);
//        queryWrapper.ne("state",2);
//        List<Certificate> certificates = certificateMapper.selectList(queryWrapper);
//        for (Certificate certificate:certificates)
//        {
//            System.out.println(certificate);
//        }



        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id","2016144844");
        queryWrapper.ne("state",2);
        queryWrapper.ne("state",0);
        Certificate caByID = certificateMapper.selectOne(queryWrapper);
        System.out.println(caByID);
    }



    @Test
    public void count()
    {
        System.out.println(certificateMapper.countApply());
        System.out.println(certificateMapper.countCa());
        System.out.println(certificateMapper.countCaByUidForUser(31));
        System.out.println(certificateMapper.countApplyByUidForUser(31));
        System.out.println(certificateMapper.selectCount(null));
    }

    @Test
    public void en()
    {
        List<Certificate> certificates = certificateMapper.selectList(null);
        int count = 0;
        for (Certificate certificate:certificates)
        {
//            if (certificate.getPassword()!=null)
//                certificate.setPassword(AESForSQL.encrypt(certificate.getPassword()));
//            if (certificate.getAlias()!=null)
//                certificate.setAlias(AESForSQL.encrypt(certificate.getAlias()));
//            if (certificate.getDescription()!=null)
//                certificate.setDescription(AESForSQL.encrypt(certificate.getDescription()));
//            if (certificate.getUsername()!=null)
//                certificate.setUsername(AESForSQL.encrypt(certificate.getUsername()));
            System.out.println(++count);
            System.out.println(certificate);
            System.out.println(certificateMapper.updateById(certificate));
        }
    }


    @Test
    public void test()
    {
        QueryWrapper<Certificate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username","user1234");
        queryWrapper.ne("state",0);
        queryWrapper.ne("state",2);

    }
}
