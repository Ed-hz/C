package com.secure.practice.utils;



import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSA {

    private static final String KEYALGORITHM = "RSA";

    private static final String PUBLICKEY = "RSAPublicKey";
    private static final String PRIVATEKEY = "RSAPrivateKey";

    //rsa私钥  或者可从配置文件读取。
//    public static final String DECRYPTPRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIMzJa4oZpQcPhRDTIaWnF4olSaeGt5oV0XFwoeeSK+FZ3lc4N34523tdfasgba";

    private RSA(){super();}

    /**
     * 将输入内容转换成base64格式
     * @param key 转换的内容，也可以是密钥
     * @return base64的字节数组
     */
    public static byte[] decryptBASE64(String key) {
        Base64 base64 = new Base64();
        return base64.decode(key);
    }

    /**
     * 将base64数组转成字符串
     * @param bytes 要转换的base64字节数组
     * @return 转换结果
     */
    public static String encryptBASE64(byte[] bytes) {
        Base64 base64 = new Base64();
        return base64.encodeToString(bytes);
    }


    /**
     * 使用私钥加密base64字节数组
     * @param data base64字节数组
     * @param key 私钥
     * @return 加密后base64字节数组
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key){
        try {

            byte[] keyBytes = decryptBASE64(key);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEYALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        }catch (Exception e){
//            log.error("RSAUtilsPrivateKeyDecryptError");
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key){
        return decryptByPrivateKey(decryptBASE64(data), key);
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key){
        try {

            byte[] keyBytes = decryptBASE64(key);

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEYALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        }catch (Exception e){
//            log.error("RSAUtilsPublicKeyDecryptError");
            e.printStackTrace();
            return new byte[0];
        }

    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key) {
        try {

            byte[] keyBytes = decryptBASE64(key);

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEYALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data.getBytes());
        }catch (Exception e){
//            log.error("RSAUtilsPublicKeyEncryptError");
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key){
        try {

            byte[] keyBytes = decryptBASE64(key);

            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEYALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        }catch (Exception e){
//            log.error("RSAUtilsPrivateKeyEncryptError");
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap){
        if(keyMap != null){
            Key key = keyMap.get(PRIVATEKEY);
            return encryptBASE64(key.getEncoded());
        }else{
            return "";
        }
    }

    /**
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap){
        if(keyMap != null){
            Key key = keyMap.get(PUBLICKEY);
            return encryptBASE64(key.getEncoded());
        }else {
            return "";
        }
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey(){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator
                    .getInstance(KEYALGORITHM);
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            Map<String, Key> keyMap = new HashMap(2);
            keyMap.put(PUBLICKEY, keyPair.getPublic());
            keyMap.put(PRIVATEKEY, keyPair.getPrivate());
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
//            log.error("RSAUtilsInitKeyError");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 签名
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, String privateKey) throws Exception {

        byte[] keyBytes = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, String publicKey, String sign) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    /**
     * 验签
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

}