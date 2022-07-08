package com.secure.practice.controller;

import com.secure.practice.entity.Certificate;
import com.secure.practice.entity.User;
import com.secure.practice.entity.vo.Page;
import com.secure.practice.entity.vo.Recall;
import com.secure.practice.entity.vo.Request;
import com.secure.practice.service.ICertificateService;
import com.secure.practice.service.IUserService;
import com.secure.practice.service.ex.CaNotExistException;
import com.secure.practice.service.ex.CaTypeException;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.JsonResult;
import com.secure.practice.utils.ca.CertUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("ca")
public class CAController extends BaseController{
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ICertificateService certificateService;
    @Autowired
    private IUserService userService;
    private final String CA = "CA/";
    String CAKeystore = CA+"CA.keystore";
    private final String personal = "personal/";
    private final String commercial = "commercial/";
    private final String rootCer = CA+"root.cer";
    @PostMapping("apply")
    public JsonResult<String> apply(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        data = AES.decrypt(data, priKey);
        Certificate certificate = JSONObject.parseObject(data, Certificate.class);
        User userByUid = userService.findUserByUid(uid);
        String msg;
        if (certificateService.add(userByUid, userByUid, certificate) == 1) {
            msg = AES.encrypt("申请成功，请耐心等待审核!",priKey);
        } else {
            msg = AES.encrypt("系统发生故障，请联系管理员！",priKey);
        }
        log.write("普通用户 uid "+uid+" 申请证书"+certificate.toString());
        return new JsonResult<>(OK, msg);
    }

    @PostMapping("updateAndReapply")
    public JsonResult<String> updateAndReapply(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        data = AES.decrypt(data, priKey);
        Certificate certificate = JSONObject.parseObject(data, Certificate.class);
        User userByUid = userService.findUserByUid(uid);
        Map<String, Object> map = new HashMap<>();
        String msg;

        if (certificateService.update(userByUid,certificate)) {
            msg = "更新成功，请耐心等待审核!";
        } else {
            msg = "系统发生故障，请联系管理员！";
        }
        log.write("普通用户 uid "+uid+" 更新并重新申请证书"+certificate.toString());
        return new JsonResult<>(OK, msg);
    }
    @GetMapping("myCa")
    public JsonResult<String> myCa(@RequestHeader String Authorization , Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
         Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<Certificate> byUidForUser = certificateService.findCaByUidForUser(uid,start,count);
        log.write("普通用户 uid "+uid+" 获取证书列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(byUidForUser),priKey));
    }

    @GetMapping("myApply")
    public JsonResult<String> myApply(@RequestHeader String Authorization , Integer start, Integer count) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
//        String data = AES.decrypt(requestBody.get("data"), priKey);
        Page<Certificate> byUidForUser = certificateService.findApplyByUidForUser(uid,start,count);
        log.write("普通用户 uid "+uid+" 获取申请列表");
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(byUidForUser),priKey));
    }

    @PostMapping("getPriKey")
    public JsonResult<String> getPriKey(@RequestHeader String Authorization , @RequestBody Map<String,String> requestBody) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        String priKey = ops.get(Authorization + "-priKey");
        String data = requestBody.get("data");
        String sign = requestBody.get("sign");
        if (!vrfySign(data,sign,priKey))
            return new JsonResult<>(10000, "签名异常", AES.encrypt(JSON.toJSONString(new Date()), priKey));
        Request<JSONObject> request = JSON.parseObject(AES.decrypt(data, priKey),Request.class);
        if(request.dateAvailable())
        {
            String id = (String) request.getData().get("id");
            String key  = (String) request.getData().get("key");
            String priKey1 = certificateService.getPriKey(id, uid, key);

            if (priKey1!= null)
            {
                log.write("普通用户 uid "+uid+" 获取证书私钥，证书id："+id);
                return new JsonResult<>(OK, "获取成功",AES.encrypt(priKey1,priKey));
            }
            else
                return new JsonResult<>(6000,"获取失败");
        }
        else {
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
        }


    }

    @GetMapping("caRecalled")
    public JsonResult<String> caRecalled() throws Exception {
        Recall recall = new Recall();
        List<Certificate> caRecalled = certificateService.getCaRecalled();
        recall.init(caRecalled,CAKeystore,"CA92666", "证书中心", "root92666");
        return new JsonResult<>(OK, "获取成功" ,JSON.toJSONString(recall));
    }



    @GetMapping("searchCaByID")
    public JsonResult<String> searchCaByID(@RequestHeader String Authorization,String ID) throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String priKey = ops.get(Authorization + "-priKey");
        Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
        Certificate caByID = certificateService.findCaByIDAndUid(ID,uid);
        List<Certificate> certificates = new ArrayList<>();
        if (caByID!=null)
            certificates.add(caByID);
        log.write("普通用户 uid "+uid+" 通过id搜索证书，id"+ID);
        return new JsonResult<>(OK, "获取成功" ,AES.encrypt(JSON.toJSONString(certificates),priKey));
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
            Integer uid = Integer.valueOf(ops.get(Authorization + "-user"));
            String id = request.getData();
            User userByUid = userService.findUserByUid(uid);
            if (certificateService.recall(userByUid,id)) {
                log.write("普通用户 uid "+uid+" 通过证书id召回证书，证书id："+id);
                return new JsonResult<>(OK, "召回成功");
            }
            else
                return new JsonResult<>(8002,"召回失败");
        }
        else {
            return new JsonResult<>(9000, "客户端与服务器时间不同步，请同步后重试");
        }
    }


//    private boolean dateAvailable(Date date){
//        Date now = new Date();
//        if (Math.abs(date.getTime()-now.getTime())<60*1000)
//        {
//            return true;
//        }
//        else return false;
//    }

    @GetMapping("/downloadCaByID")
    public JsonResult<String> downloadCaByID(String id,String token, HttpServletResponse response) throws UnsupportedEncodingException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String uid = ops.get(token + "-user");
        if (uid ==null){
            return new JsonResult<>(7000, "未登录，请登陆后下载" );
        }
        Certificate certificate = certificateService.findCaByID(id);
        if (certificate != null) {
            String fileName;
            switch (certificate.getType()) {
                case 1:
                    fileName = CA + personal + certificate.getAlias() + ".cer";
                    break;
                case 2:
                    fileName = CA + commercial + certificate.getAlias() + ".cer";
                    break;
                default:
                    throw new CaTypeException("证书类型异常+" + certificate.getId());
            }
            File file = new File(fileName);
            if (file.exists()) {
                String type = "cer";
                // 设置contenttype，即告诉客户端所发送的数据属于什么类型
                response.setHeader("Content-type",type);
                String name = URLEncoder.encode(certificate.getUsername()+"-"+certificate.getId()+".cer", "UTF-8");
                response.addHeader("Content-Disposition", "attachment;fileName=" + name);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    log.write("普通用户 udi "+uid+" 通过id下载证书，id："+id);
                    return new JsonResult<>(OK, "下载成功" );
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else
                return new JsonResult<>(7000, "下载失败" );
        } else
            throw new CaNotExistException("证书不存在！");
        return new JsonResult<>(7000, "下载失败" );
    }
}
