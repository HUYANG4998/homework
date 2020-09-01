package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.service.IUserService;
import com.wxtemplate.wxtemplate.api.util.Constant;
import com.wxtemplate.wxtemplate.api.util.RedisUtils;
import com.wxtemplate.wxtemplate.api.util.Utils;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 查询我的收藏或收藏我的
     */
    @PostMapping("/selectDynamicCollection")
    public Result selectDynamicCollection(String type, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(type)) {
            List<Map<String, Object>> list = userService.selectDynamicCollection(userId, type, pageNo, pageSize);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询评论
     */
    @PostMapping("/selectComment")
    public Result selectComment(Integer pageNo, Integer pageSize, HttpServletRequest request) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId)) {
            List<Map<String, Object>> list = userService.selectComment(userId, pageNo, pageSize);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/register")
    public Result register(String phone, String password, String code) {

        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(password) && !StringUtils.isEmpty(code)) {

            return userService.register(phone, password, code);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/SignIn")
    public Result SignIn(String phone, String password) {

        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(password)) {

            return userService.SignIn(phone, password);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 忘记密码
     *
     * @param phone
     * @param code
     * @param newPassword
     * @return
     */
    @PostMapping("/forgetPassword")
    public Result forgetPassword(String phone, String code, String newPassword) {
        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(code) && !StringUtils.isEmpty(newPassword)) {
            return userService.forgetPassword(phone, code, newPassword);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public Result code(String phone) {
        if (!StringUtils.isEmpty(phone)) {
            String code = Utils.getRandomSix();
            //boolean is_success = true;
            if (true) {
                String checkCode = phone + Constant.CODE;
                redisUtils.set(checkCode, code, 60);
                return Result.success(code);
            } else {
                return Result.fail("发送失败");
            }
        } else {
            return Result.fail("请输入手机号");
        }
    }

    /**
     * 查询用户信息
     *
     * @param request
     * @return
     */
    @PostMapping("/selectUser")
    public Result selectUser(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId)) {
            User user = userService.getById(userId);
            return Result.success(user);
        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 查询单个用户信息
     *
     * @param userId
     * @return
     */
    @PostMapping("/selectUserByUserId")
    public Result selectUser(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            Map<String, Object> map = new HashMap<>();
            User user = userService.getById(userId);
            map.put("user", user);
            if (!StringUtils.isEmpty(user.getLastUserId())) {
                map.put("referrer", userService.getById(user.getLastUserId()));
            } else {
                map.put("referrer", null);
            }
            return Result.success(map);
        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public Result updateUser(User user) {
        if (user != null) {
            User user1 = userService.getById(user.getUserId());
            if (user1 != null) {
                userService.updateById(user);
                return Result.success("修改成功");
            } else {
                return Result.fail("用户不存在");
            }

        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 权限控制
     */
    @PostMapping("/updateJurisdiction")
    public Result updateJurisdiction(User user) {
        if (user != null) {
            userService.updateJurisdiction(user);
            return Result.success("修改权限成功");
        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 查询7天或31天用户新增数量
     *
     * @param
     * @return
     */
    @PostMapping("/selectDay")
    public Result selectDay(Integer number) {
        Map<String, Object> map = userService.selectSevenDay(number);
        return Result.success(map);
    }

    /**
     * 查询新增用户数
     *
     * @return
     */
    @PostMapping("/selectCountUser")
    public Result selectCountUser() {
        Map<String, Object> map = userService.selectCountUser();
        return Result.success(map);
    }

    /**
     * 查询全部用户信息
     *
     * @return
     */
    @PostMapping("/selectAllUser")
    public Result selectAllUser(String phone, String lastUserId) {
        List<User> userList = userService.selectAllUser(phone, lastUserId);
        return Result.success(userList);
    }


    /**
     * 删除用户--用户id
     *
     * @param userId
     * @return
     */
    @PostMapping("/deleteUser")
    public Result deleteUser(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            userService.deleteUser(userId);
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    /**
     * 续费会员
     */
    @PostMapping("/renewalVip")
    public Result renewalVip(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId)) {
            return userService.renewalVip(userId);
        } else {
            return Result.fail("续费失败");
        }

    }

    /**
     * 查询我的下级
     */
    @PostMapping("/selectSubordinate")
    public Result selectSubordinate(String index, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(index)) {
            return userService.selectSubordinate(userId, index);
        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 查询我的推荐人
     */
    @PostMapping("/selectMyReferees")
    public Result selectMyReferees(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId)) {
            return userService.selectMyReferees(userId);
        } else {
            return Result.fail("用户未登录");
        }
    }

    /**
     * 查询用户信息  是否是自己好友
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/selectIsFreidns")
    public Result selectAllUser(String userId, HttpServletRequest request) {
        String myUserId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(myUserId)) {
            Map<String, Object> map = userService.selectIsFriendsUser(userId, myUserId);
            if (map != null && !map.isEmpty()) {
                return Result.success(map);
            } else {
                return Result.fail("查询对象不存在");
            }
        } else {
            return Result.fail("查询失败");
        }
    }

    public void startTime() {
        userService.startTime();
    }
}
