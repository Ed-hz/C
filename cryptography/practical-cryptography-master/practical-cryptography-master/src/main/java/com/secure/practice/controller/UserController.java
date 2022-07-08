package com.secure.practice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.RealName;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Request;
import com.secure.practice.entity.vo.UserVO;
import com.secure.practice.mapper.UserMapper;
import com.secure.practice.service.IRealNameService;
import com.secure.practice.service.IUserService;
import com.secure.practice.service.ex.VerifyCodeError;
import com.secure.practice.service.ex.VerifyCodeNotExist;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.JsonResult;
import com.secure.practice.utils.RSA;
import com.secure.practice.utils.VerifyCode;
import com.secure.practice.utils.ca.CertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    IRealNameService realNameService;

    private final String imgPath = "/img/";
    private final String userImgPath = "user/";


    @PostMapping("reg")
    @ResponseBody //表示此方法的响应结果以json格式进行数据响应给到前端
    public JsonResult<Void> reg(@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        String userStr = new String(CertUtil.decodeByJKSPrivateKey(CAKeystore,CAKeyPass,rootAlisa,rootPass,RSA.decryptBASE64(data)));
        UserVO userVO= JSONObject.parseObject(userStr,UserVO.class);
        String verifyKey = userVO.getVerifyKey();
        String verifyCode = userVO.getVerifyCode();
        String code=ops.get(verifyKey);
        if (!vrfySign(data,sign,userVO.getPriKey()))
            return new JsonResult<>(10000, "签名异常");
        if (code==null)
            throw new VerifyCodeNotExist("验证码不存在错误");
        else if(!code.equalsIgnoreCase(verifyCode))
        {
            stringRedisTemplate.delete(verifyKey);
            throw new VerifyCodeError("验证码错误");
        }else
        {
            User user = userVO.getUser();
            System.out.println(user);
            userService.reg(user);
            stringRedisTemplate.delete(verifyKey);
            log.write("普通用户成功注册，user："+user);
            return new JsonResult<>(OK,"注册成功");
        }
    }



    @RequestMapping("login")
    public JsonResult<String> login(@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        byte[] bytes = CertUtil.decodeByJKSPrivateKey(CAKeystore, CAKeyPass, rootAlisa, rootPass, RSA.decryptBASE64(data));
        String user = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(user);
        String verifyKey = (String) jsonObject.get("verifyKey");
        String verifyCode = (String) jsonObject.get("verifyCode");
        String code=ops.get(verifyKey);
        if (code==null)
            throw new VerifyCodeNotExist("验证码不存在错误");
        else if(!code.equalsIgnoreCase(verifyCode))
        {
            stringRedisTemplate.delete(verifyKey);
            throw new VerifyCodeError("验证码错误");
        }else {

            String priKey = (String) jsonObject.get("privateKey");
            String random = (String) jsonObject.get("randomStr");
            String s = DigestUtils.md5DigestAsHex(data.getBytes());
            sign = AES.decrypt(sign,priKey);
            if (!s.equals(sign))
            {
                return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
            }
            random = AES.decrypt(random, priKey);
            User result = userService.login((String) jsonObject.get("username"), (String) jsonObject.get("password"));
            String token = result.getUsername() + "-" + UUID.randomUUID().toString().toUpperCase();
            ops.set(token + "-user", result.getUid().toString(), 60 * 30, TimeUnit.SECONDS);
            result.setUid(null);//不向用户传uid
            ops.set(token + "-priKey", priKey, 60 * 30, TimeUnit.SECONDS);
            Map<String, Object> map = new HashMap<>();
            map.put("randomStr", random);
            map.put("token", token);
            map.put("user", result);
            log.write("普通用户 "+result.getUsername()+" 登录成功，分配token："+token+"使用私钥："+priKey);
            return new JsonResult<>(OK, "登录成功", AES.encrypt(JSON.toJSONString(map), priKey));
        }
    }

    /**
     * 用户登录/注册校验码生成
     * 生成验证码后，将本次生成验证码操作存入redis中，有效期为3分钟
     * 键值规则为  UUID : 4位数字验证码
     * @return
     */
    @RequestMapping(path = "/getVerifyCodePic",method = RequestMethod.GET)
    public JsonResult<Map<String,String>> getVerifyCodePic() throws IOException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Map<String, String> result = new HashMap<>();
        VerifyCode code = new VerifyCode();
        // 生成验证码图片
        BufferedImage image = code.getImage();
        // 获取验证码四位数字
        String text = code.getText();
        // 验证码-键值对存入分别存入redis
        String verifyCode_key = UUID.randomUUID().toString();
        ops.set(verifyCode_key,text,60*3, TimeUnit.SECONDS);
        System.out.println(ops.get(verifyCode_key));
        System.out.println(verifyCode_key+text+60*3);
        //进行base64编码
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            ImageIO.write(image, "png", bos);
            String string = Base64Utils.encodeToString(bos.toByteArray());
            result.put("key", verifyCode_key);
            result.put("image", string);
            return new JsonResult<>(200,result);
        }catch (IOException e){
            System.out.println(e);
        }finally {
            bos.close();
        }
        return new JsonResult<>(3000,"验证码生成失败");
    }


    @GetMapping("realNameInfo")
    public JsonResult<String> realNameInfo(@RequestHeader String Authorization) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        RealName realName = realNameService.getRealNameInfo(uid);
        log.write("普通用户 uid "+uid+" 获取自身实名");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(realName),priKey));
    }

    @GetMapping("info")
    public JsonResult<String> info(@RequestHeader String Authorization) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        User userByUid = userService.findUserByUidForUser(uid);
        log.write("普通用户 uid "+uid+" 获取自身信息");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(userByUid),priKey));
    }

    @PostMapping("realNameAuthentication")
    public JsonResult<String> realNameAuthentication(@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");

        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<Map<String,String>> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if (request.dateAvailable())
        {
            realNameService.CommitRealNameInfo(uid,request.getData().get("name"),request.getData().get("id"));
            log.write("普通用户 uid "+uid+" 修改实名信息");
            return new JsonResult<>(OK, "实名认证成功");
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }


    @PostMapping("updateInfo")
    public JsonResult<String> updateInfo(@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        data = AES.decrypt(data, priKey);
        User user = JSONObject.parseObject(data, User.class);
        if (uid.equals(user.getUid()))
        {
            if (userService.update(user))
            {
                log.write("普通用户 uid "+uid+" 更新自身信息 "+user.toString());
                return new JsonResult<>(OK, "更新成功");
            }
            return new JsonResult<>(6001, "更新信息失败" );

        }
        else return new JsonResult<>(6000, "更新信息失败" );

    }


    @PostMapping("modifyPass")
    public JsonResult<String> modifyPass(@RequestHeader String Authorization, @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<Map<String,String>> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if (request.dateAvailable())
        {
            if (userService.modifyPass(uid,request.getData().get("old"),request.getData().get("new"))) {
                log.write("普通用户 uid "+uid+" 修改密码");
                return new JsonResult<>(OK, "密码修改成功");
            }
            else
                return new JsonResult<>(7001,"原密码不正确");
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }



    @PostMapping("/uploadAvatar")
    public String uploadAvatar(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString().replace("-","") + originalFilename;
        String dirPath = System.getProperty("user.dir");
        String path = imgPath+userImgPath+ newFileName;
        File destFile = new File(dirPath + path);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }try {
            file.transferTo(destFile);
            // 将相对路径返回给前端
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
