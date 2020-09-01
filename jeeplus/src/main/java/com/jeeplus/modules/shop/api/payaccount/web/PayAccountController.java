package com.jeeplus.modules.shop.api.payaccount.web;

import com.alibaba.druid.util.StringUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.shop.api.payaccount.entity.PayAccount;
import com.jeeplus.modules.shop.api.payaccount.service.PayAccountService;
import com.jeeplus.modules.shop.util.IsNull;
import com.jeeplus.modules.shop.util.Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Api(value="EarnController",description="第三方支付账号")
@RestController
@RequestMapping("${adminPath}/api/payaccount")
@CrossOrigin
public class PayAccountController {

    @Autowired
    private PayAccountService payAccountService;

    /**
     * 查询我的账号0支付宝1微信
     */
    @ApiOperation(notes = "selectPayAccountByStatus", httpMethod = "POST", value = "查询我的账号0支付宝1微信")
    @ApiImplicitParams({
            @ApiImplicitParam(name="status",value = "0支付宝1微信",required = true, paramType = "query",dataType = "string"),
    })
    @PostMapping(value = "/selectPayAccountByStatus")
    public AjaxJson selectEarnByAddtime(String status,String userId,HttpServletRequest request){
        /*String userid=(String)request.getAttribute("userid");*/

        AjaxJson j=new AjaxJson();

        try {
            if(!StringUtils.isEmpty(userId)){
                PayAccount payAccount=payAccountService.selectPayAccountByStatus(userId,status);
                if(payAccount!=null){
                    j.setSuccess(true);
                    j.setMsg("查询成功");
                    j.put("data",payAccount);
                }else{
                    j.setSuccess(true);
                    j.put("data",payAccount);
                }
            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("获取支付账号异常");
        }
        return j;

    }

    /**
     * 添加支付账号
     */
    @ApiOperation(notes = "addPayAccount", httpMethod = "POST", value = "添加支付账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "0支付宝1微信",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="account",value = "账号",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="image",value = "头像",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名字",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="openId",value = "微信openid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="aliUserId",value = "支付宝userid",required = true, paramType = "query",dataType = "string"),
    })
    @PostMapping(value = "/addPayAccount")
    public AjaxJson addPayAccount(PayAccount payAccount){

        AjaxJson j=new AjaxJson();

        try {
            if(payAccount!=null){
                PayAccount payAccount1 = payAccountService.selectPayAccountByStatus(payAccount.getUserId(), payAccount.getStatus());
                if(payAccount1==null){
                    payAccountService.addPayAccount(payAccount);
                    j.setSuccess(true);
                    j.setMsg("添加成功");
                }else{
                    j.setSuccess(false);
                    j.setMsg("不要重复添加");
                }

            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("添加支付账号异常");
        }
        return j;

    }
    /**
     * 修改我的支付账号信息
     */
    @ApiOperation(notes = "updatePayAccount", httpMethod = "POST", value = "修改我的支付账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="payId",value = "id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="account",value = "账号",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="image",value = "头像",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="name",value = "名字",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="openId",value = "微信openid",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="aliUserId",value = "支付宝userid",required = true, paramType = "query",dataType = "string"),
    })
    @PostMapping(value = "/updatePayAccount")
    public AjaxJson updatePayAccount(PayAccount payAccount){

        AjaxJson j=new AjaxJson();

        try {
            if(!IsNull.objCheckIsNull(payAccount, Arrays.asList("delFlag"),null)){
                payAccountService.updatePayAccount(payAccount);
                j.setSuccess(true);
                j.setMsg("修改成功");

            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("修改支付账号异常");
        }
        return j;
    }
    /**
     * 删除我的账号信息
     */
    @ApiOperation(notes = "deletePayAccount", httpMethod = "POST", value = "删除我的支付账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="payId",value = "id",required = true, paramType = "query",dataType = "string"),

    })
    @PostMapping(value = "/deletePayAccount")
    public AjaxJson deletePayAccount(String payId){

        AjaxJson j=new AjaxJson();

        try {
            if(!StringUtils.isEmpty(payId)){
                PayAccount payAccount=payAccountService.selectPayAccountByPayId(payId);
                if(payAccount!=null){
                    payAccountService.deletePayAccount(payId);
                    j.setSuccess(true);
                    j.setMsg("删除成功");
                }else{
                    j.setSuccess(false);
                    j.setMsg("不要重复删除");
                }

            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("删除支付账号异常");
        }
        return j;
    }
}
