package com.wxtemplate.wxtemplate.api.util;
import com.wxtemplate.wxtemplate.tools.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class NoRepeatSubmitAop {

    private static final String JWT_TOKEN_KEY = "jwt-token";

    @Pointcut("@annotation(com.wxtemplate.wxtemplate.api.util.NoRepeatSubmit)")
    public void serviceNoRepeat() {
    }
    @Autowired
    private RedisUtils redisUtils;

    @Around("serviceNoRepeat()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        if (redisUtils.get("id") == null) {
            try {
                MethodSignature signature = (MethodSignature) pjp.getSignature();
                NoRepeatSubmit noRepeatSubmit = signature.getMethod().getAnnotation(NoRepeatSubmit.class);
                // 默认1秒内统一用户同一个地址同一个参数，视为重复提交
                redisUtils.set("id","0",noRepeatSubmit.time());
                Object o = pjp.proceed();

                return o;
            }catch (Exception e){
                throw new Exception(e.getMessage(),e.getCause());
            }
        }else{
            return Result.fail("重复提交");
        }

    }

}

