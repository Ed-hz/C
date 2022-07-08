package com.secure.practice.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secure.practice.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLoginInterceptor implements HandlerInterceptor {

    StringRedisTemplate stringRedisTemplate;
    public AdminLoginInterceptor(StringRedisTemplate redisTemplate){
        this.stringRedisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //判断是否存在token
            String token = request.getHeader("Authorization");
            if (token == null) {
                returnValue(response,7000,"token不存在！");
                return false;
            } else {
                ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
                String uid = ops.get(token+"-su");
                if (uid != null)
                {
                    return true;
                }
                else{
                    returnValue(response,7001, "token无效！");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * token不存在设置返回值
     * @param response
     * @throws IOException
     */
    private void returnValue(HttpServletResponse response,int state,String msg)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JsonResult<String> result =new JsonResult<>(state,msg);
        response.getWriter().write(JSON.toJSONString(result));
    }
}
