package com.jeeplus.modules.shop.api.chat.web;
import com.alibaba.fastjson.JSON;
import com.jeeplus.modules.shop.api.payutils.wxpay.util.Md5Util;
import groovy.util.logging.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@Slf4j
public class NoRepeatSubmitAop {

    private static final String JWT_TOKEN_KEY = "jwt-token";

    @Pointcut("@annotation(com.jeeplus.modules.shop.api.chat.web.NoRepeatSubmit)")
    public void serviceNoRepeat() {
    }

    @Around("serviceNoRepeat()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("234");

     /*   HttpServletRequest request = RequestHolder.getRequest();
        String jwtToken = request.getHeader(JWT_TOKEN_KEY);
        String key = Md5Util.md5(jwtToken + "-" + request.getRequestURL().toString()+"-"+ JSON.toJSONString(request.getParameterMap()));
        if (RedisUtil.get(key) == null) {
            try {
                Object o = pjp.proceed();
                MethodSignature signature = (MethodSignature) pjp.getSignature();
                NoRepeatSubmit noRepeatSubmit = signature.getMethod().getAnnotation(NoRepeatSubmit.class);
                // 默认1秒内统一用户同一个地址同一个参数，视为重复提交
                RedisUtil.setExpire(key, "0",noRepeatSubmit.time());
                return o;
            }catch (Exception e){
                throw new ServiceException(e.getMessage(),e.getCause());
            }
        } else {
            throw new ServiceException("重复提交");
        }*/
       return pjp;
    }


}

