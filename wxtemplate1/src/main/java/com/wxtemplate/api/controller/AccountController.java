package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Account;
import com.wxtemplate.api.entity.Asset;
import com.wxtemplate.api.service.IAccountService;
import com.wxtemplate.tools.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 账户表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-11
 */
@RestController
@RequestMapping("/api/account")
@CrossOrigin
public class AccountController {

    @Resource
    private IAccountService accountService;
    /**后台  官方查询支付宝或微信  type 0微信 1支付宝*/
    @PostMapping(value = "/selectAccountXHTByDitch")
    public Result selectWebsite(String type){
        try {
            if(!StringUtils.isEmpty(type)){
                Account account= accountService.selectAccountXHTByDitch(type);
                if(account!=null){
                    return Result.success(account);
                }else{
                    return Result.fail("检索失败");
                }
            }else{
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAccountXHTByDitch fail");
        }
    }
    /**后台  添加支付宝或微信*/
    @PostMapping(value = "/addAccount")
    public Result addWebsiteWechatOrAlipay(String moneyCode,String ditch){
        try {
            if(!StringUtils.isEmpty(moneyCode)&&!StringUtils.isEmpty(ditch)){
                accountService.addAccount(moneyCode,ditch);
                return Result.success("添加成功");
            }else{
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addAccount fail");
        }
    }
    /**后台  修改支付宝或微信*/
    @PostMapping(value = "/updateAccount")
    public Result updateAccount(String accountid,String moneyCode){
        try {
            if(!StringUtils.isEmpty(accountid)&&!StringUtils.isEmpty(moneyCode)){
                accountService.updateAccount(accountid,moneyCode);
                return Result.success("添加成功");
            }else{
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateAccount fail");
        }
    }
}
