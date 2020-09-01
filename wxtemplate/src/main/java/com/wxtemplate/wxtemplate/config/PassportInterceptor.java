package com.wxtemplate.wxtemplate.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.mapper.ChatRecordMapper;
import com.wxtemplate.wxtemplate.api.mapper.DynamicMapper;
import com.wxtemplate.wxtemplate.api.util.Constant;
import com.wxtemplate.wxtemplate.api.util.JwtHelper;
import com.wxtemplate.wxtemplate.api.util.RedisUtils;
import com.wxtemplate.wxtemplate.api.util.Utils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;


@Component
@CrossOrigin
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        PrintWriter writer = null;
        String header = request.getHeader("token");
        String token=null;
        Claims parseJWT = JwtHelper.parseJWT(header, "base64security");
        String userId = (String)request.getAttribute("userId");
        JSONObject json = JSONUtil.createObj();

        if(parseJWT!=null){
            if(StringUtils.isEmpty(userId)){
                request.setAttribute("userId", parseJWT.get("userId"));
            }
            token=parseJWT.get("userId")+"_"+parseJWT.get("username")+ Constant.TOKENSUFFIX;
        }else{
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                writer = response.getWriter();
                json.put("code",550);
                json.put("successs",false);
                json.put("message","您的账号已过期！请重新登录！");
                writer.print(json);

            } finally {
                if (writer != null)
                    writer.close();
            }
        }
        if(!StringUtils.isEmpty(header)&&!StringUtils.isEmpty(token)){
            boolean hasKey = redisUtils.hasKey(token);
            if(hasKey){
                String redisToken = redisUtils.get(token);
                if(header.equals(redisToken)){
                    redisUtils.set(token, redisToken,3600*24*15);
                    return true;
                }else{
                    try {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        writer = response.getWriter();
                        json.put("code",550);
                        json.put("successs",false);
                        json.put("message","您的账号在其他设备登录！请重新登录！");
                        writer.print(json);

                    } finally {
                        if (writer != null)
                            writer.close();
                    }
                }
            }else{
                try {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    writer = response.getWriter();
                    json.put("code",550);
                    json.put("successs",false);
                    json.put("message","您的账号已过期！请重新登录！");
                    writer.print(json);

                } finally {
                    if (writer != null)
                        writer.close();
                }
            }
        }
        return false;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        if(("/api/dynamic/selectDynamicByDynamicId").equals(requestURI)){
            SqlSession sqlsession = Utils.openSession();
            DynamicMapper dynamicMapper = sqlsession.getMapper(DynamicMapper.class);
            Map<String, String[]> parameterMap = request.getParameterMap();
            String dynamicIds = parameterMap.get("dynamicId")[0];
            Dynamic dynamic = dynamicMapper.selectById(dynamicIds);
            dynamic.setVisitNumber(dynamic.getVisitNumber()+1);
            dynamicMapper.updateById(dynamic);
        }
    }
}

 