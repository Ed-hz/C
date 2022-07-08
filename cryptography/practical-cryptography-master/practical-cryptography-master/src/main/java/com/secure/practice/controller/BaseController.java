package com.secure.practice.controller;

import com.secure.practice.service.ex.*;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.JsonResult;
import com.secure.practice.utils.Log;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {
    public static final int OK = 200;
//    String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4si1uJ/rLeM7G0k4/Nni4S2BUsc7EzZ281+lxxzOg5+XTkC18Cv/mhMUBgUcGHJPPiD+wIBeiQyKwuHH8OiitH2RBfc6J4dwSZgKHEcz3Gg0cyk+xA6yKgunbwn6q1g+UPG91jRU6GXyg6wUOdPxpy8/bB6GZE+WLrY75wMvJu/VhC+M50nnrWJXcAmHbgKnDvEH8fBST3CWemt/Ws4uQp/dCH+ltzYMZF6M1EdX+z28sIY7YcuF5U9FJk5/p5ZBlnfYjO/GhbPfSlfne0m1FOeHdpy9ogzNO7IX05aK3yKWJJecN5PfTzPonjHAyg4VmFzbh7+/YXzbNN1kMaSNswIDAQAB";
//    String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDiyLW4n+st4zsbSTj82eLhLYFSxzsTNnbzX6XHHM6Dn5dOQLXwK/+aExQGBRwYck8+IP7AgF6JDIrC4cfw6KK0fZEF9zonh3BJmAocRzPcaDRzKT7EDrIqC6dvCfqrWD5Q8b3WNFToZfKDrBQ50/GnLz9sHoZkT5YutjvnAy8m79WEL4znSeetYldwCYduAqcO8Qfx8FJPcJZ6a39azi5Cn90If6W3NgxkXozUR1f7Pbywhjthy4XlT0UmTn+nlkGWd9iM78aFs99KV+d7SbUU54d2nL2iDM07shfTlorfIpYkl5w3k99PM+ieMcDKDhWYXNuHv79hfNs03WQxpI2zAgMBAAECggEAbXPNMLQN/3Gh/0NRu8c1FtSti9QYEOfCdSd+jSC8Ew6dKelVZfr2O9hlW5pvuuTAlg26phHOjnp9Jh1uMDk2/cF0ktqFOLrHWN2GU6uRvhiU59fKMTmeh2hkgNTiJHxMZyilJJLRP/CkISSWBmriQHwvMfFxj0xSAW12b8I7qaBCXJUQlN45ebAQp7IN6uHGGPWATcSF5x/yKamJvEQpb1pUOA7JVKCKN0IGpTE8zCPuBuQIr/6aJXf0FMPvFHImhhtqTC0rY7L/UXZv7ET5DtRFzo4jo+/1i0SfyXkyx5i3wFEVlIFJHE/d8jr3yS/VoIrl2MoS3pqHYVWknfe14QKBgQD2E6pwJIuHjdaZ3iBKBQAA8CljlhQBdDr6vTDgnf59mDHtyMNI3NeqqSSnujuyaPvdZyRv02bXJYC/SsAOTEx1W2boXIgTsVjC9h1p49S2KlxGuXZUC1UFVLgmTEWo9NOAgu+smiketyLkFlTuLUZ3pqeRZTlu++p24PLlgrnNaQKBgQDr7eDFVZpv7DANuxG+SOnoMLfuJVXmeiLekB4m1z7qHi+NfdZ6QO59QEyCzaznQ4yn0qHqeTXFZP71YaJzjhLZxv0On884Dxy3k439LFK3gAFl2W3xI7H1KvYIqMRPpEFEFIu017dWP2KhnDPUHdfUdaEZ3gVvQpzIwQqkmMUyuwKBgQD0OchqzJp+ytM2mzLIw/Wg+LrbT9RDLSxsNuEPzT8LP8YuDZdj9WtGweDTZw5gn7l5oCiVo+bpmRsSwAmlJyyrPTABZfTYNqe1t7axpaEzuw8iUmeSOj0DsXWi7QgmC/buEQX29HnjNje20EMysFTD4+9jamd6MyQdIF1yVDA8IQKBgQDBFwX/22izM0W85x7Fcp1leAIA+TONluZU6vSSa1XFfIEEtznDAsNtZSN5ZmWdPK6wZ3Y3FY7JiDgWkhrHoj6RWAeiYW7R/aROJoht7UmhfzUlq0cMtV8fPVLxkVZhrBfyZTJWBrq47tWFWPceInKTItZ/+jLOdWEl+MACKDo0owKBgQDLasmtsu8eYhZN/VZlOXXck4EmGr7RdQYcUlwfOdbcCvWVAqXEVuc2YutlhQFI1b4XR3BnAtIsyyHpb2Vsqk4UTOW0mqenIGOcak2ebkxVCUuFasuKUpff3yvf6Mx/4kg/m42Gj7xt14MOyVBA21T5i9vB8MTBnrU/YdUPc9HzVw==";
    String path = "CA/";
    String CAKeystore = path+"CA.keystore";
    String CAKeyPass = "CA92666";
    String rootAlisa = "证书中心";
    String rootPass = "root92666";
    String rootCer = path+"root.cer";
    String testCer = path+"test.cer";
    String personalCer = path + "personal.cer";
    String commercialCer = path +"commercial.cer";
    Log log = new Log("log.txt");
    @ExceptionHandler()
    public JsonResult<Void> handleException(Throwable e)
    {
        JsonResult<Void> result = new JsonResult<>();
        if (e instanceof UsernameDuplicatedException)
        {
//            result=new JsonResult<>(4000,"用户名已经被占用");
            result.setState(4000);
            result.setMessage("用户名已经被占用异常");
        }
        else if (e instanceof IllegalUsernameException)
        {
            result.setState(4001);
            result.setMessage(e.getMessage());
        }
        else if(e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("注册时产生未知的异常");
//            result=new JsonResult<>(5000,"注册时产生未知的异常");
        }
        else if (e instanceof UsernameNotFoundException){
            result.setState(5001);
            result.setMessage("用户数据不存在异常");
//            result=new JsonResult<>(5001,"用户数据不存在");
        }
        else if(e instanceof PasswordNotMatchException){
            result.setState(5002);
            result.setMessage("用户名或密码错误");
//            result=new JsonResult<>(5002,"用户名的密码错误");
        }
        else if(e instanceof SelectException){
            result.setState(5003);
            result.setMessage("查询用户异常");
//            result=new JsonResult<>(5003,"查询用户异常");
        }
        else if (e instanceof VerifyCodeNotExist)
        {
            result.setState(3001);
            result.setMessage("验证码已过期");
        }
        else if (e instanceof VerifyCodeError)
        {
            result.setState(3002);
            result.setMessage("验证码错误");
        }
        else if (e instanceof TokenNotAvailableException)
        {
            result.setState(2000);
            result.setMessage("Token已过期");
        }
        else {
            result=new JsonResult<>();
        }
        log.write(e.getMessage());
        return result;
    }
    protected boolean vrfySign(String msg,String sign,String priKey) throws Exception {
        String s = AES.decrypt(sign,priKey);
        String s1 = DigestUtils.md5DigestAsHex(msg.getBytes());
        return s1.equals(s);
    }
}
