package com.wxtemplate.api.controller;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.wxtemplate.api.entity.*;
import com.wxtemplate.api.entity.VO.OwnerCertVo;
import com.wxtemplate.api.service.IChatService;
import com.wxtemplate.api.service.IOrdersService;
import com.wxtemplate.api.util.*;
import com.wxtemplate.api.service.ICarService;
import com.wxtemplate.api.service.IUserService;
import com.wxtemplate.tools.MyStaticMap;
import com.wxtemplate.tools.Result;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {



    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICarService carService;
    @Autowired
    private IChatService chatService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 个人认证   需要事务
     *
     * @param vo
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/authentication")
    public Result authentication(OwnerCertVo vo, HttpServletRequest request) {

        try {
            if (vo != null) {
                userService.authentication(vo);
                return Result.success("认证成功");
            } else {
                return Result.fail("对象为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("authentication fail");
        }
    }

    /**
     * 企业认证
     */
    @PostMapping(value = "/license")
    public Result license(License license) {
        try {
            if (license != null) {
                userService.license(license);
                return Result.success("认证成功");
            } else {
                return Result.fail("对象为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("license fail");
        }
    }

    /**
     * 获取认证信息
     */
    @PostMapping(value = "/getAuthentication")
    public Result getAuthentication(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        try {
            if (!StringUtils.isEmpty(userid)) {
                Map<String, Object> map = userService.getAuthentication(userid);
                if (map.get("realname") != null && map.get("license") != null) {
                    return Result.success(map);
                } else if (map.get("realname") == null && map.get("license") != null) {
                    return Result.fail("请按照步骤进行个人认证", map);
                } else if (map.get("realname") != null && map.get("license") == null) {
                    return Result.fail("请按照步骤进行企业认证", map);
                }
                return Result.fail("请按照步骤进行个人认证和企业");
            } else {
                return Result.fail("用户id为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("getAuthentication fail");
        }
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public Result index(String phone, String status) {
        String code = "159835";
        try {
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(status)) {
                User user = userService.findUserByPhone(phone);
                if (user == null || "1".equals(status)) {
                    /**发送验证码*/
                    code = Utils.getRandomSix();
                    boolean is_success = Aliyunsms.sendVarifyMessage(phone, code);
                    if (is_success) {
                        String checkCode = phone + "_xht_code";
                        redisUtils.set(checkCode, code,60);
                        return Result.success("发送成功");
                    } else {
                        return Result.fail("发送失败");
                    }

                } else {
                    return Result.fail("手机号已注册");
                }

            } else {
                log.info("code fail");
                return Result.fail("请输入手机号");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送验证码失败");
        }
        return Result.fail("发送验证码失败");
    }

    /**
     * 登录  携带手机号和验证码
     */
    @PostMapping("/SignIn")
    public Result login(String phone, String password) {
        String token = null;
        try {
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(password)) {
                /**验证手机号是否注册*/
                User user = userService.findUserByPhone(phone);
                if (user != null) {
                    if("0".equals(user.getIsuser())){
                        return Result.fail("您的账号已被永久封禁，无法登录！");
                    }

                    /**通过密码生成token*/
                    if (user.getPassword().equals(password)) {
                        token = JwtHelper.createJWT(user.getPhone(), user.getUserid(), 365 * 24 * 60 * (60 * 1000), "base64security");
                        String tokenRedis = user.getUserid() + "_" + user.getPhone() + "_xht_User";
                        redisUtils.set(tokenRedis, token);

                        Map<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        map.put("user", user);
                        map.put("chat", chatService.getUserAndService(user.getUserid()));
                        return Result.success(map);
                    } else {
                        return Result.fail("手机号或密码错误");
                    }
                } else {
                    return Result.fail("手机号未注册");
                }
            }
        } catch (Exception e) {
            log.error("登录失败!");
            e.printStackTrace();
        }
        return Result.fail("登录失败");
    }

    /**
     * 注册  携带手机号和验证码
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/register")
    public Result register(String phone, String code, String password) {

        try {
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(code) && !StringUtils.isEmpty(password)) {
                /**验证是否通过*/
                User user = userService.findUserByPhone(phone);
                if (user == null) {
                    /**未被注册过*/
                    String checkCode = phone + "_xht_code";
                    if (redisUtils.hasKey(checkCode)) {
                        /**验证码是否正确*/
                        if (code.equals(redisUtils.get(checkCode))) {
                            userService.addUser(phone, password);
                            return Result.success();
                        } else {
                            return Result.fail("验证码错误，请重新输入");
                        }
                    } else {
                        return Result.fail("验证码已失效，请重新发送");
                    }
                } else {
                    return Result.fail("手机号已注册");
                }
            } else {
                return Result.fail("手机号或密码为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("注册失败");
        }
        return Result.fail("注册失败");
    }

    /**
     * 修改密码  携带手机号和验证码
     */
    @PostMapping(value = "/forgetPassword")
    public Result updatePassword(String phone, String code, String newPassword) {
        try {
            if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(code) && !StringUtils.isEmpty(newPassword)) {
                /**验证是否通过*/
                User user = userService.findUserByPhone(phone);
                if (user != null) {
                    String checkCode = phone + "_xht_code";
                    if (redisUtils.hasKey(checkCode)) {
                        /**验证码是否正确*/
                        if (code.equals(redisUtils.get(checkCode))) {
                            /**验证码是否正确*/
                            userService.updateUser(phone, newPassword);
                            return Result.success();
                        } else {
                            return Result.fail("验证码错误，请重新输入");
                        }
                    } else {
                        return Result.fail("验证码未发送或已失效，请重新发送");
                    }
                } else {
                    return Result.fail("手机号未被注册");
                }
            } else {
                return Result.fail("手机号或密码为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("修改密码失败");
        }
        return Result.fail("修改密码失败");
    }

    /**
     * 添加个人车辆
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/AuthenticationCar")
    public Result AuthenticationCar(Car car, HttpServletRequest request) {
        String carPhotos = request.getParameter("carPhotos");
        try {
            if (car != null && !StringUtils.isEmpty(carPhotos)) {
                userService.AuthenticationCar(car, carPhotos);
                return Result.success();
            } else {
                return Result.fail("车辆信息为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("AuthenticationCar fail");
        }
    }

    /**
     * 删除个人车辆
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/deleteCar")
    public Result deleteCar(String carid) {
        try {
            if (!StringUtils.isEmpty(carid)) {
                userService.deleteCar(carid);
                return Result.success();
            } else {
                return Result.fail("无关键条件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("出现异常");
        }
    }


    /**
     * 发布订单
     */
    @PostMapping(value = "/createOrder")
    public Result createOrder(Orders orders) {
        try {
            if (orders != null) {
                userService.createOrder(orders);
                return Result.success();
            } else {
                return Result.fail("订单信息不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("getAuthentication fail");
        }
    }

    /**
     * 我发布的所有类型
     * userid
     * orderstatus
     */
    @PostMapping(value = "/order")
    public Result order(String orderstatus, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderstatus) && !StringUtils.isEmpty(userid)) {
                List<Map<String, Object>> map = userService.order(orderstatus, userid);
                return Result.success(map);
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("order fail");
        }
    }

    /**
     * 我发布的  我接到的  我预定的 查看车辆信息
     * orderid
     * cartype   main deupty
     */
    @PostMapping(value = "/orderViewCarInfo")
    public Result orderViewCarInfo(@RequestBody Map<String, Object> map) {
        try {
            if (map != null && !map.isEmpty()) {
                List<Map<String, Object>> result = userService.orderViewCarInfo(map);
                return Result.success(result);
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("order fail");
        }
    }

    /**
     * 我接到的 我所提供的车辆   查询
     */
    @PostMapping(value = "/selectMyOrderCar")
    public Result selectMyOrderCar(String orderid, String type, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderid) && !StringUtils.isEmpty(userid) && !StringUtils.isEmpty(type)) {
                List<Map<String, Object>> map = userService.selectMyOrderCar(orderid, userid, type);
                return Result.success(map);
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectMyOrderCar fail");
        }
    }

    /**
     * 我接到的 订单方支付的订单
     */
    @PostMapping(value = "/payorderinfo")
    public Result payorderinfo(String orderid, String orderpaystatus, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");

        try {
            if (!StringUtils.isEmpty(orderid) && !StringUtils.isEmpty(userid) && !StringUtils.isEmpty(orderpaystatus)) {
                List<Map<String, Object>> map = userService.payorderinfo(orderid, userid, orderpaystatus);
                return Result.success(map);
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("payorderinfo fail");
        }
    }

    /**
     * 最新的订单详情   发布订单之后跳转的页面
     */
    @PostMapping(value = "/selectNewOrderDetail")
    public Result selectNewOrderDetail(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid)) {
                Map<String, Object> map = userService.selectNewOrderDetail(userid);
                if (map != null && !map.isEmpty()) {
                    return Result.success(map);
                } else {
                    return Result.fail("检索失败");
                }

            } else {
                return Result.fail("用户id为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectNewOrderDetail fail");
        }
    }

    /**
     * 我的发布  取消订单
     */
    @PostMapping(value = "/cancelAllOrder")
    @Transactional(rollbackFor = Exception.class)
    public Result cancelAllOrder(String orderid, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderid) && !StringUtils.isEmpty(userid)) {
                userService.cancelAllOrder(orderid, userid);
                return Result.success("取消成功");
            } else {
                return Result.fail("订单id为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("cancelOrder fail");
        }
    }

    /**
     * 我的发布  评价车主
     */
    @PostMapping(value = "/releaseEval")
    public Result cancelAllOrder(Commentuser commentuser, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (commentuser != null && !StringUtils.isEmpty(userid)) {
                userService.releaseEval(commentuser, userid);
                return Result.success("评价成功");
            } else {
                return Result.fail("评价失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("cancelOrder fail");
        }
    }


    /**
     * 我的预定  评价订单
     */
   /* @PostMapping(value = "/releaseEval")
    public Result cancelAllOrder(Commentuser commentuser,HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        *//*String userid="234";*//*
        try {
            if(commentuser!=null&&!StringUtils.isEmpty(userid)){
                userService.releaseEval(commentuser,userid);
                return Result.success("评价成功");
            }else{
                return Result.fail("评价失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("cancelOrder fail");
        }
    }*/

    /**
     * 订单方支付已接订单的车辆   让车主状态进入待支付保证金状态
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/myReleasePayOrderInfo")
    public Result myReleasePayOrderInfo(String orderinfoid, String pay, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderinfoid) && !StringUtils.isEmpty(userid) && !StringUtils.isEmpty(pay)) {
                boolean is_beyond = userService.isBeyondRes(orderinfoid);
                boolean is_chat = false;
                if (is_beyond) {
                    if ("1".equals(pay)) {
                        //余额支付
                        boolean is_success = userService.myReleasePayOrderInfo(orderinfoid, userid);
                        if (is_success) {
                            return Result.success("支付成功");
                        } else {
                            return Result.fail("余额不足");
                        }
                    } else if ("3".equals(pay)) {
                        //微信支付
                        is_chat = true;
                    }
                    Map<String, Object> map = userService.pay(orderinfoid, is_chat);
                    return Result.success(map);

                } else {
                    return Result.fail("已超出订单需求");
                }
            } else {
                return Result.fail("对象为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("myReleasePayOrderInfo fail");
        }
    }

    /**
     * 我接到的  查询4种状态的订单
     */
    @PostMapping(value = "/selectTakeMyOrderInfo")
    public Result selectTakeMyOrderInfo(@RequestParam Map<String, Object> map) {

        try {
            if (map != null) {
                List<Map<String, Object>> listMap = userService.selectTakeMyOrderInfo(map);

                return Result.success(listMap);


            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectTakeMyOrderInfo fail");
        }
    }

    @PostMapping(value = "/selectIsSign")
    public Result selectIsSign(String orderid, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        try {
            if (!StringUtils.isEmpty(userid) && !StringUtils.isEmpty(orderid)) {
                boolean is_success = userService.selectIsSign(userid, orderid);
                return Result.success(is_success);
            } else {
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectTakeMyOrderInfo fail");
        }
    }

    /**
     * 我接到的   抢单
     */
    @PostMapping(value = "/addMyOrderInfo")
    public Result addMyOrderInfo(@RequestBody Map<String, List<Orderinfo>> map) {
        List<Orderinfo> orderinfoList = map.get("orderinfoList");
        try {
            if (orderinfoList != null) {
                userService.addMyOrderInfo(orderinfoList);
                return Result.success();
            } else {
                return Result.fail("用户id为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectNewOrderDetail fail");
        }
    }

    /**
     * 我接到的 查询订单 订单详情
     */
    @PostMapping(value = "/selectMyOrderInfoDetail")
    public Result selectMyOrderInfoDetail(String orderid) {
        try {
            if (!StringUtils.isEmpty(orderid)) {
                Map<String, Object> map = userService.selectMyOrderInfoDetail(orderid);
                if (map != null && !map.isEmpty()) {
                    return Result.success(map);
                } else {
                    return Result.fail("检索失败");
                }

            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectMyOrderInfoDetail fail");
        }
    }

    /**
     * 我接到的  取消全部订单  待付保证金状态
     * orderid订单id
     * type取消状态0已接单1待付保证金
     */
    @PostMapping(value = "/cancelAllOrderinfo")
    @Transactional(rollbackFor = Exception.class)
    public Result cancelAllOrderinfo(String orderid, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderid) && !StringUtils.isEmpty(userid)) {
                userService.cancelAllOrderinfo(orderid, userid);
                return Result.success("取消成功");
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("cancelAllOrderinfo fail");
        }
    }

    /**
     * 我接到的  取消单个订单
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/cancelOrderinfo")
    public Result cancelOrderinfo(String orderinfoid) {
        try {
            if (!StringUtils.isEmpty(orderinfoid)) {
                userService.cancelOrderinfo(orderinfoid);
                return Result.success();
            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("cancelOrderinfo fail");
        }
    }

    /**
     * 我接到的 支付保证金
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/takeMyOrderInfoMargin")
    public Result takeMyOrderInfoMargin(String orderinfoid, String pay, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderinfoid) && !StringUtils.isEmpty(userid) && !StringUtils.isEmpty(pay)) {
                boolean ischat = false;
                if ("1".equals(pay)) {
                    boolean is_success = userService.takeMyOrderInfoMargin(orderinfoid, userid);
                    if (is_success) {
                        userService.detectionOrder(orderinfoid);
                        return Result.success("支付保证金成功");
                    } else {
                        return Result.fail("余额不足");
                    }
                } else if ("3".equals(pay)) {
                    //微信支付
                    ischat = true;
                }
                Map<String, Object> map = userService.pay(orderinfoid, ischat);
                return Result.success(map);

            } else {
                return Result.fail("订单为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("takeMyOrderInfoMargin fail");
        }
    }

    /**
     * 联系车主
     * orderid
     * time
     */
    @PostMapping(value = "/contactDriver")
    public Result contactDriver(@RequestParam Map<String, Object> map) {
        try {
            if (map != null && !map.isEmpty()) {
                String time = map.get("time") == null ? null : map.get("time").toString();
                String orderid = map.get("orderid") == null ? null : map.get("orderid").toString();
                Date date = DateUtil.parse(time, "yyyy-MM-dd");
                Date date1 = DateUtil.parse(DateUtil.now(), "yyyy-MM-dd");
                long day = DateUtil.between(date, date1, DateUnit.DAY);

                if (day > 2) {
                    return Result.fail("目前不可查看，行程前两天可查看车主联系方式");
                }
                if (!StringUtils.isEmpty(orderid)) {
                    List<Map<String, Object>> driverMap = userService.contactDriver(map);
                    return Result.success(driverMap);
                } else {
                    return Result.fail("订单为空");
                }
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("order fail");
        }
    }

    /**
     * 查询全部行程
     */
    @PostMapping(value = "/selectTrip")
    public Result selectTrip(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid)) {
                List<Trip> tripList = userService.selectTrip(userid);

                return Result.success(tripList);

            } else {
                return Result.fail("该用户无已有行程");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectTrip fail");
        }
    }

    /**
     * 查询单个行程
     */
    @PostMapping(value = "/selectTripByTripId")
    public Result selectTripByTripId(String tripid) {
        try {
            if (!StringUtils.isEmpty(tripid)) {
                Trip trip = userService.selectTripByTripId(tripid);
                if (trip != null) {
                    return Result.success(trip);
                } else {
                    return Result.fail("查询为空");
                }
            } else {
                return Result.fail("检索失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectTripByTripId fail");
        }
    }

    /**
     * 修改行程
     */
    @PostMapping(value = "/updateTripByTripId")
    public Result updateTripByTripId(Trip trip) {
        try {
            if (trip != null) {
                userService.updateTripByTripId(trip);
                return Result.success("修改成功");
            } else {
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateTripByTripId fail");
        }
    }

    /**
     * 新增行程
     */
    @PostMapping(value = "/addTrip")
    public Result addTrip(Trip trip, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (trip != null) {
                userService.addTrip(trip, userid);
                return Result.success("添加成功");
            } else {
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addTrip fail");
        }
    }

    /**
     * 删除行程
     */
    @PostMapping(value = "/deleteTrip")
    public Result deleteTrip(String tripid) {
        try {
            if (!StringUtils.isEmpty(tripid)) {
                userService.deleteTrip(tripid);
                return Result.success("删除成功");
            } else {
                return Result.fail("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteTrip fail");
        }
    }

    /**
     * 加入车库
     */
    @PostMapping(value = "/addGarag")
    public Result addGarag(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid) && map != null && !map.isEmpty()) {
                userService.addGarag(userid, map);
                return Result.success("添加成功");
            } else {
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addGarag fail");
        }
    }

    /**
     * 删除车库车辆
     */
    @PostMapping(value = "/deleteGarag")
    public Result deleteGarag(String[] garageidList1) { //@RequestParam List<String> garageidList,
        try {
            if (garageidList1.length > 0) {
                List<String> garageidList = Arrays.asList(garageidList1);
                userService.deleteGarag(garageidList);
                return Result.success("删除成功");
            } else {
                return Result.fail("删除失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("deleteGarag fail");
        }
    }

    /**
     * 查询车库所有车辆
     */
    @PostMapping(value = "/myGarag")
    public Result selectGarag(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="047b57674ec24f81bbf6ca97b55255c8";*/
        try {
            if (!StringUtils.isEmpty(userid)) {
                List<Map<String, Object>> garagList = userService.selectGarag(userid);

                return Result.success(garagList);


            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectGarag fail");
        }
    }

    /**
     * 我的预定 所有4种状态的所有订单
     */
    @PostMapping(value = "/selectOrderByOrderInfo")
    public Result selectOrderByOrderInfo(@RequestBody Map<String, Object> map) {
        try {
            if (map != null) {
                List<Map<String, Object>> ordersList = userService.selectOrderByOrderInfo(map);

                return Result.success(ordersList);

            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrderByOrderInfo fail");
        }
    }

    /**
     * 我的预定 订单详情
     */
    @PostMapping(value = "/selectOrdersByOrderId")
    public Result selectOrdersByOrderId(String orderid) {
        try {
            if (!StringUtils.isEmpty(orderid)) {
                Map<String, Object> orders = userService.selectOrdersByOrderId(orderid);
                if (orders != null && !orders.isEmpty()) {
                    return Result.success(orders);
                } else {
                    return Result.fail("无订单信息");
                }
            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrdersByOrderId fail");
        }
    }

    /**
     * 我的预定  支付订单  orders 订单  paystatus 支付状态 0未支付1支付
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/reservationPay")
    public Result reservationPay(@RequestBody Map<String, Object> map, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInfo = objectMapper.writeValueAsString(map.get("order"));
        Orders order = objectMapper.readValue(jsonInfo, Orders.class);

        String pay = map.get("pay") == null ? null : map.get("pay").toString();
        List<Map<String, Object>> carList = map.get("carlist") == null ? null : (List<Map<String, Object>>) map.get("carlist");
        /* Orders order=map.get("order")==null?null:(Orders)map.get("order");*/
        /*String userid=(String)request.getAttribute("userid");*/
        /*String userid="234";*/
        try {
            if (order != null && !StringUtils.isEmpty(pay)) {
                //判断是否拥有库存
                boolean is_inventory = userService.checkinventory(carList);
                //余额支付是否成功
                boolean is_pay_success = false;
                //支付宝
                boolean is_pay = false;
                //微信
                boolean is_chat = false;
                //判断是否是余额支付
                if (is_inventory) {
                    if ("1".equals(pay)) {
                        //余额支付
                        is_pay_success = userService.balancePay(order, carList);
                    } else if ("2".equals(pay)) {
                        //支付宝
                        is_pay = true;
                    } else if ("3".equals(pay)) {
                        is_chat = true;
                    }
                    Map<String, Object> is_success = userService.reservationPay(order, carList, is_pay_success, is_pay, is_chat);
                    if ("余额不足".equals(is_success.get("order").toString())) {
                        return Result.fail(is_success.get("order").toString());
                    } else {
                        return Result.success(is_success);
                    }

                } else {
                    return Result.fail("车辆不足");
                }
            } else {
                return Result.fail("请补充对象信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("订单生成失败");
        }
    }

    /**
     * 我的预定 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/cancelOrderinfoReserve")
    public Result cancelOrderinfoReserve(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String orderid = map.get("orderid") == null ? null : map.get("orderid").toString();
        String type = map.get("type") == null ? null : map.get("type").toString();
        List<Map<String, Object>> carList = map.get("carlist") == null ? null : (List<Map<String, Object>>) map.get("carlist");
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(orderid) && !StringUtils.isEmpty(type) && carList != null) {

                userService.cancelOrderinfoReserve(orderid, type, carList, userid);
                return Result.success("取消成功");
            } else {
                return Result.fail("取消失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("取消订单失败");
        }
    }

    /**
     * 我的预定 查看主车副车信息
     */
    @PostMapping(value = "/reserveCarOrders")
    public Result reserveCarOrders(@RequestBody Map<String, Object> map, HttpServletRequest request) {

        try {
            if (map != null && !map.isEmpty()) {

                List<Map<String, Object>> listMap = userService.reserveCarOrders(map);
                return Result.success(listMap);
            } else {
                return Result.fail("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("查询失败");
        }
    }

    /**
     * 我的评价
     */
    @PostMapping(value = "/myEval")
    public Result myEval(String userid) {
        try {
            if (!StringUtils.isEmpty(userid)) {
                List<Map<String, Object>> evalMap = userService.myEval(userid);
                return Result.success(evalMap);
            } else {
                return Result.fail("获取评价失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取评价失败");
        }
    }

    /**
     * 我的意见反馈
     */
    @PostMapping(value = "/myFeedback")
    public Result myFeedback(Fendback fendback, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid) && fendback != null) {
                userService.myFeedback(userid, fendback);
                return Result.success("反馈成功");
            } else {
                return Result.fail("反馈失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("反馈失败");
        }
    }

    /**
     * 收益明细   根据时间  未完成
     */
    @PostMapping(value = "/selectEarnByAddtime")
    public Result selectEarnByAddtime(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/

        String createtime = null;
        int pagesize = Integer
                .parseInt(map.get("pagesize") == null ? "10" : map.get("pagesize").toString());
        int pagenum = Integer
                .parseInt(map.get("pagenum") == null ? "1" : map.get("pagenum").toString());
        String addtime = map.get("addtime") == null ? null : map.get("addtime").toString();
        /*if(!StringUtils.isEmpty(addtime)){
            createtime=DateUtil.format(DateUtil.parse(addtime),"yyyy-MM");
           *//* = new SimpleDateFormat("yyyy-MM").format();*//*
        }*/
        try {
            PageHelper.startPage(pagenum, pagesize, true, null, true);
            if (!StringUtils.isEmpty(userid)) {
                List<Earn> earnList = userService.selectEarnByAddtime(userid, addtime);

                return Result.success(earnList);

            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("myFeedback fail");
        }
    }

    /**
     * 昨日收益和当前金额
     */
    @PostMapping(value = "/yesterdayEarn")
    public Result yesterdayEarn(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="2345";*/
        try {
            if (!StringUtils.isEmpty(userid)) {
                Map<String, Object> earnAndprice = userService.yesterdayEarnAndprice(userid, DateUtil.formatDate(DateUtil.yesterday()));
                if (earnAndprice != null && !earnAndprice.isEmpty()) {
                    return Result.success(earnAndprice);
                } else {
                    return Result.fail("无收益");
                }
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("yesterdayEarn fail");
        }
    }

    /**
     * 个人支付宝或微信信息添加
     */
    @PostMapping(value = "/addAccount")
    public Result addAsset(@RequestBody Account account, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (account != null && !StringUtils.isEmpty(userid)) {
                userService.addAccount(userid, account);
                return Result.success("添加成功");
            } else {
                return Result.fail("添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addAccount fail");
        }
    }

    /**
     * 修改个人提现支付宝或微信
     */
    @PostMapping(value = "/updateAccount")
    public Result updateAsset(@RequestBody Account account) {

        try {
            if (account != null) {
                userService.updateAccount(account);
                return Result.success("修改成功");
            } else {
                return Result.fail("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateAsset fail");
        }
    }

    /**
     * 查询个人提现支付宝或微信 ditch 0微信1支付宝
     */
    @PostMapping(value = "/selectAccount")
    public Result selectAsset(String ditch, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid) && !StringUtils.isEmpty(ditch)) {
                Account account = userService.selectAccount(userid, ditch);

                return Result.success(account);

            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAsset fail");
        }
    }

    /**
     * 用户提交充值或提现申请 0充值1提现
     */
    @PostMapping(value = "/addAssetByRechargeOrWithdrawal")
    public Result selectAsset(Asset asset, HttpServletRequest request) {
        /*String userid=(String)request.getAttribute("userid");*/
        /*String userid="234";*/
        try {
            if (asset != null) {
                boolean success = userService.addAsset(asset);
                if (success) {
                    return Result.success("提交成功");
                } else {
                    return Result.fail("余额不足");
                }


            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addAssetByRechargeOrWithdrawal fail");
        }
    }

    /**
     * 我的 查询用户信息、星级、车主认证
     */
    @PostMapping(value = "/selectUser")
    public Result selectUser(HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="980033e54673482a8083ba0ff9be4489";*/
        try {
            if (!StringUtils.isEmpty(userid)) {
                User user = userService.selectUser(userid);
                if (user != null) {
                    return Result.success(user);
                } else {
                    return Result.fail("检索失败");
                }
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectUser fail");
        }
    }

    /**
     * 已接到  到达目的地  看是否迟到
     */
    @PostMapping(value = "/carReach")
    public Result carReach(String orderid, HttpServletRequest request) {
        String userid = (String) request.getAttribute("userid");
        /*String userid="234";*/
        try {
            if (!StringUtils.isEmpty(userid) && !StringUtils.isEmpty(orderid)) {
                return userService.carReach(userid, orderid);

            } else {
                return Result.fail("到达失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("carReach fail");
        }
    }

    @PostMapping(value = "/extendsTime")
    @Transactional(rollbackFor = Exception.class)
    public void extendsTime() {
        try {
            /* userService.carReach(userid,orderid,marrytime);*/
            userService.extendsTime();
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * 已接到的   过去60分钟之后处理
     */
    @PostMapping(value = "/carAbsent")
    public Result carAbsent(String orderid, HttpServletRequest request) {

        try {
            if (!StringUtils.isEmpty(orderid)) {
                userService.carAbsent(orderid);
                return Result.success("处理成功");
            } else {
                return Result.fail("处理成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("carAbsent fail");
        }
    }

    /**
     * 预定进行中手动完成
     */
    @PostMapping(value = "/orderAutoComplete")
    @Transactional(rollbackFor = Exception.class)
    public Result orderAutoComplete(String orderid, HttpServletRequest request) {

        try {
            if (!StringUtils.isEmpty(orderid)) {
                userService.orderAutoComplete(orderid);
                return Result.success("完成成功");
            } else {
                return Result.fail("完成失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderAutoComplete fail");
        }
    }

    /**
     * 我的发布进行中手动完成
     */
    @PostMapping(value = "/orderManualComplete")
    @Transactional(rollbackFor = Exception.class)
    public Result orderManualComplete(String orderinfoid, HttpServletRequest request) {

        try {
            if (!StringUtils.isEmpty(orderinfoid)) {
                userService.orderManualComplete(orderinfoid);
                return Result.success("手动完成成功");
            } else {
                return Result.fail("手动完成失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderManualComplete fail");
        }
    }

    /**
     * 车主完成结算
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/orderCompleteSettlement")
    public Result orderCompleteSettlement(List<String> orderinfoidList, HttpServletRequest request) {
        try {
            if (orderinfoidList != null) {
                userService.orderCompleteSettlement(orderinfoidList);
                return Result.success("完成结算");
            } else {
                return Result.fail("结算失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderCompleteSettlement fail");
        }
    }

    /**
     * 预定完成结算
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/orderComplete")
    public Result orderComplete(String orderid, HttpServletRequest request) {
        try {
            if (!StringUtils.isEmpty(orderid)) {
                userService.orderComplete(orderid);
                return Result.success("完成结算");
            } else {
                return Result.fail("完成结算");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderComplete fail");
        }
    }
    /**
     * 我接到的  接单时查询不在接亲时间段里的个人车辆
     */
   /* @PostMapping(value = "/selectCarByNotMarrayTime")
    public Result selectCarByNotMarrayTime(String marraytime,String hour,HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        /*String userid="123";*//*
        try {
            if(!StringUtils.isEmpty(marraytime)&&!StringUtils.isEmpty(hour)&&!StringUtils.isEmpty(userid)){
                List<Car> carList=userService.selectCarByNotMarrayTime(marraytime,hour,userid);
                if(carList.size()>0){
                    return Result.success(carList);
                }else{
                    return Result.fail("检索失败");
                }

            }else{
                return Result.fail("查看失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectCarByNotMarrayTime fail");
        }
    }*/

    /**
     * 查询用户
     */
    @PostMapping(value = "/selectUserList")
    public Result selectUserList(String phone, HttpServletRequest request) {
        try {

            List<User> userList = userService.selectUserList(phone);
            return Result.success(userList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectUserList fail");
        }
    }

    /**
     * 查询单个用户
     */
    @PostMapping(value = "/selectUserByUserId")
    public Result selectUserByUserId(String userid, HttpServletRequest request) {

        try {
            if (!StringUtils.isEmpty(userid)) {
                User user = userService.selectUserByUserId(userid);
                if (user != null) {
                    return Result.success(user);
                } else {
                    return Result.fail("检索失败");
                }
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectUserByUserId fail");
        }
    }

    /**
     * 修改单个用户
     */
    @PostMapping(value = "/updateUser")
    public Result updateUser(User user, HttpServletRequest request) {

        try {
            if (user != null) {
                userService.updateUser(user);
                return Result.success("修改成功");
            } else {
                return Result.fail("条件不足");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateUser fail");
        }
    }


}
