package com.secure.practice.utils;


import com.secure.practice.utils.ca.CertUtil;
import com.secure.practice.utils.ca.DigitalCertificateGenerator;
import com.secure.practice.utils.ca.KeyStoreInfo;
import com.secure.practice.utils.ca.SignedCertInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;


import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Date;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CATests {
    String path = "CA/";
    String CAKeystore = path+"CA.keystore";
    String rootCer = path+"root.cer";
    String testCer = path+"test.cer";
    String personalCer = path + "personal.cer";
    String commercialCer = path +"commercial.cer";
    @Test
    public void init(){
        System.out.println("开始初始化证书库!");
        KeyStoreInfo certInfo = new KeyStoreInfo("证书中心", "CA92666", "root92666", "BinW", "HIT",
                "IS", "Harbin", "HeiLongJiang", "China", new Date(), 365, CAKeystore);
        DigitalCertificateGenerator.generateJKS(certInfo);
        System.out.println("初始化完成！");
        System.out.println("根证书别名为证书中心，用户名为BinW，证书库密码为CA92666，根证书密码为root92666");
    }

    @Test
    public void exportRootCer() { //导出根证书
        System.out.println("开始导出根证书！");
        // 证书库路径,库密码,别名,cer证书路径
        DigitalCertificateGenerator.exportJKSPublicKeyCertificate(
                CAKeystore, "CA92666", "证书中心", rootCer);
        System.out.println("导出根证书成功！");
    }
    @Test
    public void testSignCert() {// 根据根证书签发证书
        // 签发证书的信息
        SignedCertInfo signedCertInfo = new SignedCertInfo();
        signedCertInfo.setC("USA");
        signedCertInfo.setCN("BinW2");
        signedCertInfo.setL("NewYork");
        signedCertInfo.setO("MIT");
        signedCertInfo.setOU("CS");
        signedCertInfo.setST("NewYorkState");
        signedCertInfo.setSubjectAlias("测试证书");
        signedCertInfo.setSubjectAliasPass("test92666");
        signedCertInfo.setIssuerAlias("证书中心");
        signedCertInfo.setIssuerAliasPass("root92666");// 证书颁发者证书密码
        signedCertInfo.setKeyStorePass("CA92666");// 颁发者的所在证书库
        signedCertInfo.setKeyStorePath(CAKeystore);// 颁发者证书库路径
        signedCertInfo.setSubjectPath(testCer);// 存储签发证书的路径
        signedCertInfo.setValidity(365 * 2);// 有效期,单位:天
        System.out.println(signedCertInfo);
        // 签发证书("测试证书"的证书),并且存储到证书库("CurrentTest.keystore")
        DigitalCertificateGenerator.signCertJKSForSubject(signedCertInfo);
    }

    @Test
    public void testSignPersonal() {// 根据根证书签发证书
        // 签发证书的信息
        SignedCertInfo signedCertInfo = new SignedCertInfo();
        signedCertInfo.setC("CH");
        signedCertInfo.setCN("Personal");
        signedCertInfo.setL("Harbin");
        signedCertInfo.setO("HIT");
        signedCertInfo.setOU("IS");
        signedCertInfo.setST("HeiLongJiang");
        signedCertInfo.setSubjectAlias("personal");
        signedCertInfo.setSubjectAliasPass("personal92666");
        signedCertInfo.setIssuerAlias("证书中心");
        signedCertInfo.setIssuerAliasPass("root92666");// 证书颁发者证书密码
        signedCertInfo.setKeyStorePass("CA92666");// 颁发者的所在证书库
        signedCertInfo.setKeyStorePath(CAKeystore);// 颁发者证书库路径
        signedCertInfo.setSubjectPath(personalCer);// 存储签发证书的路径
        signedCertInfo.setValidity(365 * 2);// 有效期,单位:天
        System.out.println(signedCertInfo);
        DigitalCertificateGenerator.signCertJKSForSubject(signedCertInfo);
    }
    @Test
    public void testSignCommercial() {// 根据根证书签发证书
        // 签发证书的信息
        SignedCertInfo signedCertInfo = new SignedCertInfo();
        signedCertInfo.setC("CH");
        signedCertInfo.setCN("Commercial");
        signedCertInfo.setL("Harbin");
        signedCertInfo.setO("HIT");
        signedCertInfo.setOU("IS");
        signedCertInfo.setST("HeiLongJiang");
        signedCertInfo.setSubjectAlias("commercial");
        signedCertInfo.setSubjectAliasPass("commercial92666");
        signedCertInfo.setIssuerAlias("证书中心");
        signedCertInfo.setIssuerAliasPass("root92666");// 证书颁发者证书密码
        signedCertInfo.setKeyStorePass("CA92666");// 颁发者的所在证书库
        signedCertInfo.setKeyStorePath(CAKeystore);// 颁发者证书库路径
        signedCertInfo.setSubjectPath(commercialCer);// 存储签发证书的路径
        signedCertInfo.setValidity(365 * 2);// 有效期,单位:天
        System.out.println(signedCertInfo);
        DigitalCertificateGenerator.signCertJKSForSubject(signedCertInfo);
    }

    @Test
    public void testPublicKeyInCert() {// 获取cer证书的公钥
        System.out.println("证书文件中root公钥内容如下：");
        System.out.println(CertUtil.publicKeyInCert(rootCer));
        System.out.println("证书库中root公钥内容如下：");
        System.out.println(CertUtil.publicKeyInJKS(CAKeystore, "CA92666", "证书中心"));

//        System.out.println("证书文件中test公钥内容如下：");
//        System.out.println(CertUtil.publicKeyInCert(testCer));
//        System.out.println("证书库中test公钥内容如下：");
//        System.out.println(CertUtil.publicKeyInJKS(CAKeystore, "CA92666", "测试证书"));
    }

    @Test
    public void testPrivateKey() {// 根据证书别名,获取证书库中该证书的私钥
        System.out.println(CertUtil.privateKeyInJKS(CAKeystore, "CA92666", "证书中心", "root92666"));
        System.out.println(CertUtil.privateKeyInJKS(CAKeystore, "CA92666", "测试证书", "test92666"));
    }

    @Test
    public void testCertVerifing() {// 验证签发证书的签名
        try {
            CertUtil.verifySign(rootCer, testCer);
            System.out.println("test.cer通过root.cer验证");
        }catch (Exception e)
        {
            System.err.println("test.cer为通过root.cer验证");
        }
    }

    @Test
    public void testCertValidityDays() {// 验证有效期,即证书没有过期,到当前时间有效.
        System.out.println("开始检测test证书是否过期");
        CertUtil.verifyValidityDays(testCer);
        System.out.println("验证结束，证书在有效期内");
    }

    @Test
    public void testID()
    {
        System.out.println(CertUtil.getSerialNumber(personalCer));
    }

    @Test
    public void testGetAllAliasesInfo() {// 获取证书库中所有证书别名
        System.out.println(CertUtil.allAliasesInJKS(CAKeystore, "CA92666"));
    }

    @Test
    public void testKeyStoreEncodeAndDecode() {// 根据证书库中的证书(私钥公钥),加密解密

//        String msg = "这是使用证书库中测试证书加密的内容";
//        System.out.println("开始测试使用证书库中的证书加解密，消息内容为：");
//        System.out.println(msg);
        String msg = "这是使用证书库中测试证书加密的内容";
        System.out.println("开始测试使用证书库中的证书加解密，消息内容为：");
        System.out.println(msg);
        // 私钥加密
        System.out.println("使用证书库中test证书私钥加密，加密后信息内容如下：");
        byte[] data = CertUtil.encodeByJKSPrivateKey(CAKeystore, "CA92666", "测试证书", "test92666", msg.getBytes());
        System.out.println(new String(data));
        // 公钥解密
        System.out.println("使用证书库中test证书公钥解密，解密后信息内容如下：");
        data = CertUtil.decodeByJKSPublicKey(CAKeystore, "CA92666", "测试证书", data);
        System.out.println(new String(data));


        System.out.println("==============");


        //公钥加密
        System.out.println("使用证书库中test证书公钥加密，加密后信息内容如下：");
        data = CertUtil.encodeByJKSPublicKey(CAKeystore, "CA92666", "测试证书", msg.getBytes());
        System.out.println(new String(data));
        //私钥解密
        System.out.println("使用证书库中test证书私钥加密，加密后信息内容如下：");
        data = CertUtil.decodeByJKSPrivateKey(CAKeystore, "CA92666", "测试证书", "test92666", data);
        System.out.println(new String(data));

        System.out.println("测试结束！");
    }

    @Test
    public void testCerFileEncodeAndDecode() {// 公钥证书cer的加密解密

        String msg = "这是使用test.cer加密的内容";
        // cer证书加密
        byte[] encodeBytes = CertUtil.encodeByCert(testCer, msg.getBytes());
        System.out.println("使用test.cer加密，加密后信息内容如下：");
        System.out.println(new String(encodeBytes));
        // 用其相关的私钥解密
        byte[] decodeBytes = CertUtil.decodeByJKSPrivateKey(CAKeystore, "CA92666", "测试证书", "test92666", encodeBytes);
        System.out.println("使用证书库中私钥解密，解密后信息内容如下：");
        System.out.println(new String(decodeBytes));

        System.out.println("=============================");

        // 用其相关的私钥加密

        encodeBytes = CertUtil.encodeByJKSPrivateKey(CAKeystore, "CA92666", "测试证书", "test92666", msg.getBytes());
        System.out.println("使用证书库中私钥加密，加密后信息内容如下：");
        System.out.println(new String(encodeBytes));
        // cer证书解密
        decodeBytes = CertUtil.decodeByCert(testCer, encodeBytes);
        System.out.println("使用test.cer解密，解密后信息内容如下：");
        System.out.println(new String(decodeBytes));

    }


    @Test
    public void getPrikeyTest() throws Exception {
        //派生私钥
        String password = "123456";//用户自定义的密码
        String result = DigestUtils.md5DigestAsHex((password).getBytes()).toUpperCase();
        System.out.println("md5变换后生成的加密私钥的私钥"+result);


        //测试服务器发送AES加密过的私钥，用户
        PrivateKey privateKey = CertUtil.privateKeyInJKS(CAKeystore, "CA92666", "证书中心", "root92666");
        byte[] prikey = privateKey.getEncoded();
        String priKeyStr = RSA.encryptBASE64(prikey);
        System.out.println("从证书库中获取到私钥为\n"+priKeyStr);
        String priKeyStrC= AES.encrypt(priKeyStr,result);
        System.out.println("返回给用户加密过的私钥为\n"+priKeyStrC);
        String priKeyCustomer = AES.decrypt(priKeyStrC,result);
        System.out.println("用户解密后的私钥为\n"+priKeyCustomer);

        //测试客户端私钥加密，用证书解密
        String priEnc = "测试私钥加密";
        String priEncC = RSA.encryptBASE64(RSA.encryptByPrivateKey(priEnc.getBytes(),priKeyCustomer));
        System.out.println("密文为\n"+priEncC);
        byte[] decodeBytes = CertUtil.decodeByCert(rootCer, RSA.decryptBASE64(priEncC));
        System.out.println("解密后明文为\n"+new String(decodeBytes));


        //测试证书加密，客户端私钥解密
        String priDec = "测试私钥解密";
        byte[] bytes = CertUtil.encodeByCert(rootCer, priDec.getBytes());
        String priDecC = RSA.encryptBASE64(bytes);
        System.out.println("证书加密后密文为\n"+priDecC);
        byte[] bytes1 = RSA.decryptByPrivateKey(RSA.decryptBASE64(priDecC), priKeyCustomer);
        System.out.println("密钥解密为\n"+new String(bytes1));
    }




}
