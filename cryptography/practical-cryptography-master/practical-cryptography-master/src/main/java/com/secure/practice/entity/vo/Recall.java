package com.secure.practice.entity.vo;

import com.alibaba.fastjson.JSON;
import com.secure.practice.entity.Certificate;
import com.secure.practice.utils.AES;
import com.secure.practice.utils.ca.CertUtil;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recall {
    private String timeAndKey;
    private String data;

    public void init(List<Certificate> list, String CAKeystore, String storePass, String alias, String certPass) throws Exception {
        TimeAndKey timeAndKey = new TimeAndKey();
        timeAndKey.setDate(new Date());
        timeAndKey.setKey(AES.getKey());
        byte[] bytes = CertUtil.encodeByJKSPrivateKey(CAKeystore, storePass, alias, certPass, JSON.toJSONString(timeAndKey).getBytes(StandardCharsets.UTF_8));
        this.timeAndKey = Base64.encodeBase64String(bytes);
        this.data = AES.encrypt(JSON.toJSONString(list),timeAndKey.getKey());
    }

    public String getTimeAndKey() {
        return timeAndKey;
    }

    public void setTimeAndKey(String timeAndKey) {
        this.timeAndKey = timeAndKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Set<String> getSet(String rootCer) throws Exception {
        byte[] bytes = CertUtil.decodeByCert(rootCer, Base64.decodeBase64(timeAndKey));
        String s = new String(bytes);
        TimeAndKey timeAndKey = JSON.parseObject(s,TimeAndKey.class);
        String decrypt = AES.decrypt(data, timeAndKey.getKey());
        List<Certificate> list = JSON.parseObject(decrypt,List.class);
        Set<String> set = new HashSet<>();
        for (Certificate c:list)
        {
            set.add(c.getId());
        }
        return set;
    }

    public boolean valid(String rootCer)
    {
        byte[] bytes = CertUtil.decodeByCert(rootCer, Base64.decodeBase64(timeAndKey));
        String s = new String(bytes);
        TimeAndKey timeAndKey = JSON.parseObject(s,TimeAndKey.class);
        return timeAndKey.valid();
    }
}
