package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Enduser;
import com.wxtemplate.api.entity.User;
import com.wxtemplate.api.service.IEnduserService;
import com.wxtemplate.api.util.JwtHelper;
import com.wxtemplate.api.util.PaginationUtil;
import com.wxtemplate.api.util.RedisUtils;
import com.wxtemplate.tools.MyStaticMap;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-22
 */
@RestController
@RequestMapping("/api/enduser")
@CrossOrigin
public class EnduserController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IEnduserService enduserService;
    /**
     * 后台  修改密码
     */
    @RequestMapping(value="pcupPassword",method= RequestMethod.POST)
    public Map<String,Object> pcupPassword(@RequestBody Map<String,Object> map, HttpServletRequest request){
        String newPassword=map.get("newPassword")==null?null:map.get("newPassword").toString();
        String oldPassword=map.get("oldPassword")==null?null:map.get("oldPassword").toString();
        Map<String, Object> result=new HashMap<String,Object>();
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                /**查询我的订单*/
                Enduser enduser=enduserService.findUser(userid);
                String password=enduser.getEndpassword();
                if(password.equals(oldPassword)){
                    enduser.setEndpassword(newPassword);
                    enduserService.updateUser(enduser);
                    result= PaginationUtil.generateNormalJSON(null, 1, "修改成功");
                }else{
                    result= PaginationUtil.generateNormalJSON(null, 0, "旧密码错误");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result= PaginationUtil.generateNormalJSON(null, 0, e.getMessage());
        }
        return result;
    }

    /**
     * 登录  携带手机号和验证码
     */
    @PostMapping("/SignIn")
    public Result login1(String phone, String password){
        String token=null;
        try {
            if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(password)){
                /**验证手机号是否注册*/
                Enduser enduser = enduserService.findUserPhone(phone);
                if(enduser!=null){

                    /**通过密码生成token*/
                    if(enduser.getEndpassword().equals(password)){
                        token = JwtHelper.createJWT(enduser.getEndphone(), enduser.getEnduserid(),365*24*60*(60*1000), "base64security");
                        String tokenRedis=enduser.getEnduserid()+"_"+enduser.getEndphone()+"_xht_User";
                        redisUtils.set(tokenRedis, token);

                        Map<String,Object> map=new HashMap<>();
                        map.put("token",token);
                        map.put("user",enduser);
                        return Result.success(map);
                    }else{
                        return Result.fail("用户名或密码错误");
                    }
                }else{
                    return  Result.fail("用户名未注册");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("登录失败");
    }
    /**
     * 注册  携带手机号和验证码
     */
    /*@Transactional(rollbackFor=Exception.class)
    @PostMapping(value="/register")
    public Result register(String phone,String password){

        try {
            if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(password)){
                *//**验证是否通过*//*
                Enduser user = enduserService.findUserPhone(phone);
                if(user==null){

                    enduserService.addEndUser(phone,password);
                    return Result.success("注册成功");

                }else{
                    return Result.fail("用户名已注册");
                }
            }else{
                return Result.fail("用户名或密码为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return Result.fail("注册失败");
    }*/
    @PostMapping(value="/selectEndUser")
    public Result selectEndUser(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                /**验证是否通过*/
                Enduser user = enduserService.findUserID(userid);
                if(user!=null){
                    return Result.success(user);
                }else{
                    return Result.fail("没有此用户信息");
                }
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUser fail");
        }
    }
    @PostMapping(value="/selectEndUserByUserid")
    public Result selectEndUserByUserid(String userid){
        try {
            if(!StringUtils.isEmpty(userid)){
                /**验证是否通过*/
                Enduser user = enduserService.findUserID(userid);
                if(user!=null){
                    return Result.success(user);
                }else{
                    return Result.fail("没有此用户信息");
                }
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUserByUserid fail");
        }
    }
    @PostMapping(value="/updateEndUser")
    public Result updateEndUser(@RequestBody Enduser user){
        try {
            if(user!=null){
                /**验证是否通过*/
                enduserService.updateEndUser(user);
                 return Result.success("修改成功");
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateEndUser fail");
        }
    }
    @PostMapping(value="/addEndUser")
    public Result addEndUser(@RequestBody Enduser user){
        try {
            if(user!=null){
                /**验证是否通过*/
                Enduser enduser=enduserService.findUserPhone(user.getEndphone());
                if(enduser==null){
                    enduserService.addEndUser(user);
                    return Result.success("新增成功");
                }else{
                    return Result.fail("账号已注册");
                }

            }else{
                return Result.fail("参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addEndUser fail");
        }
    }
    @PostMapping(value="/deleteEndUser")
    public Result deleteEndUser(String userid){
        try {
            if(!StringUtils.isEmpty(userid)){
                /**验证是否通过*/
                enduserService.deleteEndUser(userid);
                return Result.success("删除成功");
            }else{
                return Result.fail("参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteEndUser fail");
        }
    }
    @PostMapping(value="/selectEndUserList")
    public Result selectEndUserList(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                List<Enduser> enduserList=enduserService.selectEndUser(userid);
                return Result.success(enduserList);
            }else{
                return Result.fail("用户未登录");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUserList fail");
        }
    }
}
