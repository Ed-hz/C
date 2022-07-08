package com.secure.practice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.RealName;
import com.secure.practice.entity.Su;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;
import com.secure.practice.entity.vo.Request;
import com.secure.practice.entity.vo.UserVO;
import com.secure.practice.service.ICertificateService;
import com.secure.practice.service.IRealNameService;
import com.secure.practice.service.ISuService;
import com.secure.practice.service.IUserService;
import com.secure.practice.service.ex.VerifyCodeError;
import com.secure.practice.service.ex.VerifyCodeNotExist;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.AESForSQL;
import com.secure.practice.utils.JsonResult;
import com.secure.practice.utils.RSA;
import com.secure.practice.utils.ca.CertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("su")
public class SuController extends BaseController {
    @Autowired
    private ISuService suService;
    @Autowired
    private IUserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private IRealNameService realNameService;

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
            if (!vrfySign(data,sign,priKey))
                return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
            random = AES.decrypt(random, priKey);
            Su result = suService.login((String) jsonObject.get("username"), (String) jsonObject.get("password"));
            String token = result.getUsername() + "-" + UUID.randomUUID().toString().toUpperCase();
            ops.set(token + "-su", result.getUid().toString(), 60 * 30, TimeUnit.SECONDS);
            result.setUid(null);//不向用户传uid
            ops.set(token + "-priKey", priKey, 60 * 30, TimeUnit.SECONDS);
            Map<String, Object> map = new HashMap<>();
            map.put("randomStr", random);
            map.put("token", token);
            map.put("user", result);
            log.write("管理员用户 "+result.getUsername() +"成功登录，分配token:"+token+" 使用私钥："+priKey);
            return new JsonResult<>(OK, "登录成功", AES.encrypt(JSON.toJSONString(map), priKey));
        }
    }


    @RequestMapping("getUsers")
    public JsonResult<String> getUsers (@RequestHeader String Authorization,Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<User> result=userService.getAllUsers(start,count);
        log.write("管理员用户 uid"+uid+" 获取用户列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(result),priKey));
    }

    /**
     * 搜索用户，通过用户名
     * @param
     * @return
     */
    @PostMapping("searchUser")
    public JsonResult<String> searchUser (@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<String> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if (request.dateAvailable())
        {
            User result = suService.searchUser(request.getData());
//        String data = AES.decrypt(requestBody.get("data"), priKey);
            List<User> list =new ArrayList<>();
            if (result!=null)
                list.add(result);
            log.write("管理员用户 uid "+uid+" 通过用户名"+request.getData()+"搜索用户");
            return new JsonResult<>(OK, "搜索成功" ,AES.encrypt(JSON.toJSONString(list),priKey));
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }
    @RequestMapping("delUser")
    public JsonResult<String> deleteUser(@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<String> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if (request.dateAvailable())
        {
            Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
            Su su = suService.getUserByUid(uid);
            if(suService.deleteUser(request.getData(),su)) {
                log.write("管理员用户 uid "+uid+" 通过用户名"+request.getData()+"注销用户");
                return new JsonResult<>(OK, "注销成功");
            }
            else
                return new JsonResult<>(OK, "注销失败");
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }
    @RequestMapping("recUser")
    public JsonResult<String> recUser(@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<String> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if (request.dateAvailable())
        {
            Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
            Su su = suService.getUserByUid(uid);
            if (suService.recoverUser(request.getData(),su)) {
                log.write("管理员用户 uid "+uid+" 通过用户名"+request.getData()+"恢复用户");
                return new JsonResult<>(OK, "激活成功");
            }
            else
                return new JsonResult<>(OK, "激活失败");
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }


    @PostMapping("updateUser")
    public JsonResult<String> updateUser(@RequestHeader String Authorization,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        data = AES.decrypt(data, priKey);
        User user = JSONObject.parseObject(data, User.class);
        Su su = suService.getUserByUid(uid);
        if (suService.updateUser(su,user))
        {
            log.write("管理员用户 uid "+uid+" 更新用户"+user.toString());
            return new JsonResult<>(OK, "更新成功");
        }
        else return new JsonResult<>(6000, "更新信息失败" );
    }
    @PostMapping("resetPass")
    public JsonResult<String> resetPass(@RequestHeader String Authorization, @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        Su su = suService.getUserByUid(uid);
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<Integer> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);

        if (request.dateAvailable())
        {
            String password = UUID.randomUUID().toString().replace("-","").substring(0,20);
            if (userService.modifyPass(request.getData(),password,su))
            {
                System.out.println(password);
                log.write("管理员用户 uid "+uid+" 重置用户密码，用户名："+request.getData());
                return new JsonResult<>(OK, "密码修改成功",AES.encrypt(password,priKey));
            }
            else
                return new JsonResult<>(7001,"原密码不正确");
        }
        else
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
    }

    @GetMapping("applyManagement")
    public JsonResult<String> applyManagement(@RequestHeader String Authorization , Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<Certificate> apply = certificateService.findApply(start, count);
        log.write("管理员用户 uid"+uid+" 获取申请列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(apply),priKey));
    }

    @GetMapping("realNameManagement")
    public JsonResult<String> realNameManagement(@RequestHeader String Authorization , Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<RealName> realNamePage = realNameService.getRealNamePage(start,count);
        log.write("管理员用户 uid"+uid+" 获取实名列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(realNamePage),priKey));
    }

    @GetMapping("caManagement")
    public JsonResult<String> caManagement(@RequestHeader String Authorization , Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<Certificate> apply = certificateService.findAfterDeal(start,count);
        log.write("管理员用户 uid "+uid+" 获取证书列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(apply),priKey));
    }
    @PostMapping("updateCa")
    public JsonResult<String> updateCa(@RequestHeader String Authorization ,@RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        data = AES.decrypt(data, priKey);
        Certificate certificate = JSONObject.parseObject(data, Certificate.class);
        Su userByUid = suService.getUserByUid(uid);
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        if (certificateService.update(userByUid,certificate)) {
            log.write("管理员用户 uid "+uid+" 更新证书"+certificate.toString());
            return new JsonResult<>(OK, "更新成功");
        }
        else
            return new JsonResult<>(8001, "更新失败");
    }

    @GetMapping("searchCaByID")
    public JsonResult<String> searchCaByID(@RequestHeader String Authorization ,String ID) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        Certificate caByID = certificateService.findCaByID(ID);
        List<Certificate> list = new ArrayList<>();
        if (caByID!=null)
            list.add(caByID);
        log.write("管理员用户 uid "+uid+" 通过证书id"+ID+"搜索证书");
        return new JsonResult<>(OK,"搜索成功",AES.encrypt(JSON.toJSONString(list),priKey));
    }

    @GetMapping("searchRealByUsername")
    public JsonResult<String> searchRealByUsername(@RequestHeader String Authorization ,String username) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        RealName realName = realNameService.searchRealByUsername(username);
        List<RealName> list = new ArrayList<>();
        if (realName!=null)
            list.add(realName);
        log.write("管理员用户 uid "+uid+" 通过用户名"+username+"搜索用户实名信息");
        return new JsonResult<>(OK,"搜索成功",AES.encrypt(JSON.toJSONString(list),priKey));
    }

    @GetMapping("searchApplyByUsername")
    public JsonResult<String> searchApplyByUsername(@RequestHeader String Authorization ,String username) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        List<Certificate> caByAlias = certificateService.findApplyByUsername(AESForSQL.encrypt(username));
        log.write("管理员用户 uid "+uid+" 通过用户名"+username+"搜索申请列表");
        return new JsonResult<>(OK,"搜索成功",AES.encrypt(JSON.toJSONString(caByAlias),priKey));
    }
    @GetMapping("searchCaByUsername")
    public JsonResult<String> searchCaByUsername(@RequestHeader String Authorization ,String username) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
        String priKey = ops.get(Authorization + "-priKey");
        List<Certificate> caByAlias = certificateService.findCaByUsername(AESForSQL.encrypt(username));
        log.write("管理员用户 uid "+uid+" 通过用户名"+username+"搜索证书列表");
        return new JsonResult<>(OK,"搜索成功",AES.encrypt(JSON.toJSONString(caByAlias),priKey));
    }

    @PostMapping("active")
    public JsonResult<String> active(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<Integer> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);

            if(request.dateAvailable())
            {
                Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
                Integer cid = (Integer) request.getData();
                Su userByUid = suService.getUserByUid(uid);
                if (certificateService.active(cid,userByUid)) {
                    log.write("管理员用户 uid "+uid+" 通过证书cid "+cid+" 激活证书");
                    return new JsonResult<>(OK, "激活成功");
                }
                else
                    return new JsonResult<>(8000,"激活失败");
            }
            else {
                return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
            }
    }
    @PostMapping("recall")
    public JsonResult<String> recall(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<String> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);

        if(request.dateAvailable())
        {
            Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
            String id = request.getData();
            Su userByUid = suService.getUserByUid(uid);
            if (certificateService.recall(userByUid,id)) {
                log.write("管理员用户 uid "+uid+" 通过证书id  "+id+" 召回证书");
                return new JsonResult<>(OK, "召回成功");
            }
            else
                return new JsonResult<>(8003,"召回失败");
        }
        else {
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
        }
    }
    @PostMapping("recallRevocation")
    public JsonResult<String> recallRevocation(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<String> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);

        if(request.dateAvailable())
        {
            Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
            String id = request.getData();
            Su userByUid = suService.getUserByUid(uid);
            if (certificateService.recallRevocation(userByUid,id)) {
                log.write("管理员用户 uid "+uid+" 通过证书id "+id+" 撤销召回证书");
                return new JsonResult<>(OK, "撤销召回成功");
            }
            else
                return new JsonResult<>(8003,"撤销召回失败");
        }
        else {
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
        }
    }
    @PostMapping("refuse")
    public JsonResult<String> refuse(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<Integer> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if(request.dateAvailable())
        {
            Integer uid = Integer.valueOf(ops.get(Authorization + "-su"));
            Integer cid = (Integer) request.getData();
            Su userByUid = suService.getUserByUid(uid);
            if (certificateService.refuse(userByUid,cid)) {
                log.write("管理员用户 uid "+uid+" 通过证书cid "+cid+" 驳回申请");
                return new JsonResult<>(OK, "驳回成功");
            }
            else
                return new JsonResult<>(8000,"驳回失败");
        }
        else {
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
        }
    }


}
