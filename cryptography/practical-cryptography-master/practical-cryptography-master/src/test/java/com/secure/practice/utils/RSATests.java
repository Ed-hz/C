package com.secure.practice.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RSATests {
    @Test
    public void GenKey() throws Exception {
        Map<String, Key> keyPair = RSA.initKey();
        System.out.println("私钥");
        System.out.println("-----BEGIN RSA PRIVATE KEY-----"+RSA.getPrivateKey(keyPair)+"-----END RSA PRIVATE KEY-----");
        System.out.println("公钥");
        System.out.println("-----BEGIN PUBLIC KEY-----"+RSA.getPublicKey(keyPair)+"-----END PUBLIC KEY-----");
        String m = "hello";
//        System.out.println(Arrays.toString(m.getBytes(StandardCharsets.UTF_8)));
//        String publicKey=RSA.getPublicKey(keyPair);
//        String privateKey=RSA.getPrivateKey(keyPair);
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4si1uJ/rLeM7G0k4/Nni4S2BUsc7EzZ281+lxxzOg5+XTkC18Cv/mhMUBgUcGHJPPiD+wIBeiQyKwuHH8OiitH2RBfc6J4dwSZgKHEcz3Gg0cyk+xA6yKgunbwn6q1g+UPG91jRU6GXyg6wUOdPxpy8/bB6GZE+WLrY75wMvJu/VhC+M50nnrWJXcAmHbgKnDvEH8fBST3CWemt/Ws4uQp/dCH+ltzYMZF6M1EdX+z28sIY7YcuF5U9FJk5/p5ZBlnfYjO/GhbPfSlfne0m1FOeHdpy9ogzNO7IX05aK3yKWJJecN5PfTzPonjHAyg4VmFzbh7+/YXzbNN1kMaSNswIDAQAB";
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDiyLW4n+st4zsbSTj82eLhLYFSxzsTNnbzX6XHHM6Dn5dOQLXwK/+aExQGBRwYck8+IP7AgF6JDIrC4cfw6KK0fZEF9zonh3BJmAocRzPcaDRzKT7EDrIqC6dvCfqrWD5Q8b3WNFToZfKDrBQ50/GnLz9sHoZkT5YutjvnAy8m79WEL4znSeetYldwCYduAqcO8Qfx8FJPcJZ6a39azi5Cn90If6W3NgxkXozUR1f7Pbywhjthy4XlT0UmTn+nlkGWd9iM78aFs99KV+d7SbUU54d2nL2iDM07shfTlorfIpYkl5w3k99PM+ieMcDKDhWYXNuHv79hfNs03WQxpI2zAgMBAAECggEAbXPNMLQN/3Gh/0NRu8c1FtSti9QYEOfCdSd+jSC8Ew6dKelVZfr2O9hlW5pvuuTAlg26phHOjnp9Jh1uMDk2/cF0ktqFOLrHWN2GU6uRvhiU59fKMTmeh2hkgNTiJHxMZyilJJLRP/CkISSWBmriQHwvMfFxj0xSAW12b8I7qaBCXJUQlN45ebAQp7IN6uHGGPWATcSF5x/yKamJvEQpb1pUOA7JVKCKN0IGpTE8zCPuBuQIr/6aJXf0FMPvFHImhhtqTC0rY7L/UXZv7ET5DtRFzo4jo+/1i0SfyXkyx5i3wFEVlIFJHE/d8jr3yS/VoIrl2MoS3pqHYVWknfe14QKBgQD2E6pwJIuHjdaZ3iBKBQAA8CljlhQBdDr6vTDgnf59mDHtyMNI3NeqqSSnujuyaPvdZyRv02bXJYC/SsAOTEx1W2boXIgTsVjC9h1p49S2KlxGuXZUC1UFVLgmTEWo9NOAgu+smiketyLkFlTuLUZ3pqeRZTlu++p24PLlgrnNaQKBgQDr7eDFVZpv7DANuxG+SOnoMLfuJVXmeiLekB4m1z7qHi+NfdZ6QO59QEyCzaznQ4yn0qHqeTXFZP71YaJzjhLZxv0On884Dxy3k439LFK3gAFl2W3xI7H1KvYIqMRPpEFEFIu017dWP2KhnDPUHdfUdaEZ3gVvQpzIwQqkmMUyuwKBgQD0OchqzJp+ytM2mzLIw/Wg+LrbT9RDLSxsNuEPzT8LP8YuDZdj9WtGweDTZw5gn7l5oCiVo+bpmRsSwAmlJyyrPTABZfTYNqe1t7axpaEzuw8iUmeSOj0DsXWi7QgmC/buEQX29HnjNje20EMysFTD4+9jamd6MyQdIF1yVDA8IQKBgQDBFwX/22izM0W85x7Fcp1leAIA+TONluZU6vSSa1XFfIEEtznDAsNtZSN5ZmWdPK6wZ3Y3FY7JiDgWkhrHoj6RWAeiYW7R/aROJoht7UmhfzUlq0cMtV8fPVLxkVZhrBfyZTJWBrq47tWFWPceInKTItZ/+jLOdWEl+MACKDo0owKBgQDLasmtsu8eYhZN/VZlOXXck4EmGr7RdQYcUlwfOdbcCvWVAqXEVuc2YutlhQFI1b4XR3BnAtIsyyHpb2Vsqk4UTOW0mqenIGOcak2ebkxVCUuFasuKUpff3yvf6Mx/4kg/m42Gj7xt14MOyVBA21T5i9vB8MTBnrU/YdUPc9HzVw==";
        byte[] bytes = RSA.encryptByPublicKey(m, publicKey);
        System.out.println(RSA.encryptBASE64(bytes));
        byte[] bytes1 = RSA.decryptByPrivateKey(RSA.encryptBASE64(bytes), privateKey);
        System.out.println(new String(bytes1, StandardCharsets.UTF_8));


//        System.out.println("公钥加密私钥解密");
//        System.out.println(Arrays.toString(m.getBytes(StandardCharsets.UTF_8)));
//        byte[] bytes = RSA.encryptByPublicKey(m.getBytes(StandardCharsets.UTF_8), RSA.getPublicKey(keyPair));
//        System.out.println(Arrays.toString(bytes));
//        byte[] bytes1 = RSA.decryptByPrivateKey(bytes, RSA.getPrivateKey(keyPair));
//        System.out.println(Arrays.toString(bytes1));
//
//
//        System.out.println("私钥加密公钥解密");
//        System.out.println(Arrays.toString(m.getBytes(StandardCharsets.UTF_8)));
//        byte[] bytes2 = RSA.encryptByPrivateKey(m.getBytes(StandardCharsets.UTF_8), RSA.getPrivateKey(keyPair));
//        System.out.println(Arrays.toString(bytes2));
//        byte[] bytes3 = RSA.decryptByPublicKey(bytes2, RSA.getPublicKey(keyPair));
//        System.out.println(Arrays.toString(bytes3));
    }
}
