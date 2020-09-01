package com.wxtemplate.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.EndUser;
import com.wxtemplate.wxtemplate.api.service.IEndUserService;
import com.wxtemplate.wxtemplate.api.util.Constant;
import com.wxtemplate.wxtemplate.api.util.JwtHelper;
import com.wxtemplate.wxtemplate.api.util.PaginationUtil;
import com.wxtemplate.wxtemplate.api.util.RedisUtils;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台用户 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/api/end-user")
@CrossOrigin
public class EndUserController {


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IEndUserService endUserService;


    /**
     * 登录  携带手机号和验证码
     */
    @PostMapping("/SignIn")
    public Result login1(String phone, String password) {
        String token = null;
        try {
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(password)) {
                /**验证手机号是否注册*/
                EndUser endUser = endUserService.findUserPhone(phone);
                if (endUser != null) {

                    /**通过密码生成token*/
                    if (endUser.getPassword().equals(password)) {
                        token = JwtHelper.createJWT(endUser.getUserName(), endUser.getEndUserId(), 365 * 24 * 3600*1000, "base64security");
                        String tokenRedis = endUser.getEndUserId() + "_" + endUser.getUserName() + Constant.TOKENSUFFIX;
                        redisUtils.set(tokenRedis, token, 1000*3600 * 24 * 30);

                        Map<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        map.put("user", endUser);
                        return Result.success(map);
                    } else {
                        return Result.fail("用户名或密码错误");
                    }
                } else {
                    return Result.fail("用户名未注册");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("登录失败");
    }

    @PostMapping(value = "/selectEndUser")
    public Result selectEndUser(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userId");
        try {
            if (!StringUtils.isEmpty(userid)) {
                /**验证是否通过*/
                EndUser user = endUserService.findUserID(userid);
                if (user != null) {
                    return Result.success(user);
                } else {
                    return Result.fail("没有此用户信息");
                }
            } else {
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUser fail");
        }
    }

    /**
     * 根据用户id查询
     *
     * @param userid
     * @return
     */
    @PostMapping(value = "/selectEndUserByUserid")
    public Result selectEndUserByUserid(String userid) {
        try {
            if (!StringUtils.isEmpty(userid)) {
                /**验证是否通过*/
                EndUser endUser = endUserService.findUserID(userid);
                if (endUser != null) {
                    return Result.success(endUser);
                } else {
                    return Result.fail("没有此用户信息");
                }
            } else {
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUserByUserid fail");
        }
    }

    /**
     * 后台修改密码
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "pcupPassword")
    public Map<String, Object> pcupPassword(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String newPassword = map.get("newPassword") == null ? null : map.get("newPassword").toString();
        String oldPassword = map.get("oldPassword") == null ? null : map.get("oldPassword").toString();
        Map<String, Object> result = new HashMap<>();
        String userid = (String) request.getAttribute("userId");
        try {
            if (!StringUtils.isEmpty(userid)) {
                /**查询我的订单*/
                EndUser enduser = endUserService.getById(userid);
                if(enduser !=null){
                    String password = enduser.getPassword();
                    if (password.equals(oldPassword)) {
                        enduser.setPassword(newPassword);
                        endUserService.updateById(enduser);
                        result = PaginationUtil.generateNormalJSON(null, 1, "修改成功");
                    } else {
                        result = PaginationUtil.generateNormalJSON(null, 0, "旧密码错误");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            result = PaginationUtil.generateNormalJSON(null, 0, e.getMessage());
        }
        return result;
    }

    /**
     * 修改后台用户信息
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/updateEndUser")
    public Result updateEndUser(@RequestBody EndUser user) {
        try {
            if (user != null) {
                /**验证是否通过*/
                endUserService.updateEndUser(user);
                return Result.success("修改成功");
            } else {
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateEndUser fail");
        }
    }

    /**
     * 添加后台用户
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/addEndUser")
    public Result addEndUser(@RequestBody EndUser user) {
        try {
            if (user != null) {
                /**验证是否通过*/
                EndUser endUser = endUserService.findUserPhone(user.getUserName());
                if (endUser == null) {
                    endUserService.addEndUser(user);
                    return Result.success("新增成功");
                } else {
                    return Result.fail("账号已注册");
                }

            } else {
                return Result.fail("参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addEndUser fail");
        }
    }

    /**
     * 删除后台用户
     *
     * @param userid
     * @return
     */
    @PostMapping(value = "/deleteEndUser")
    public Result deleteEndUser(String userid) {
        try {
            if (!StringUtils.isEmpty(userid)) {
                /**验证是否通过*/
                endUserService.deleteEndUser(userid);
                return Result.success("删除成功");
            } else {
                return Result.fail("参数为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteEndUser fail");
        }
    }

    /**
     * 查询自己不在里面的用户信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/selectEndUserList")
    public Result selectEndUserList(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userId");
        try {
            if (!StringUtils.isEmpty(userid)) {
                List<EndUser> endUserList = endUserService.selectEndUser(userid);
                return Result.success(endUserList);
            } else {
                return Result.fail("用户未登录");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectEndUserList fail");
        }
    }
}
