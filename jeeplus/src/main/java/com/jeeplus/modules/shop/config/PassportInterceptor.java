package com.jeeplus.modules.shop.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jeeplus.modules.shop.util.JwtHelper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Component
public class PassportInterceptor implements HandlerInterceptor {

    private MyStaticMap redisUtils = new MyStaticMap();
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
        String userId = (String)request.getAttribute("userid");
        JSONObject json = JSONUtil.createObj();
        if(parseJWT!=null){
            if(StringUtils.isEmpty(userId)){
                request.setAttribute("userid", parseJWT.get("userId"));
            }
            token=parseJWT.get("userId")+"_"+parseJWT.get("username")+"_mhmm_User";
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
            }
        }
        return false;
        /*return true;*/
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

 