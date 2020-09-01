package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.service.IPayService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-18
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {

    @Resource
    private IPayService payService;

    /**
     * 充值
     * @param request
     * @return
     */
    @PostMapping("/topUp")
    public Result selectUser(HttpServletRequest request,String money,String type) {
        String userId=(String)request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)){
            return payService.topUp(userId,money,type);
        }else{
            return Result.fail("用户未登录");
        }
    }
}
