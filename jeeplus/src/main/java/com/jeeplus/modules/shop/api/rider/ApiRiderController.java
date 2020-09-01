package com.jeeplus.modules.shop.api.rider;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.service.RiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lhh
 * @date 2020/1/15 0026
 */

@Api(value = "ApiRiderController", description = "App骑手端控制器")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/rider")
public class ApiRiderController extends BaseController {

    @Autowired
    private RiderService riderService;

    @ModelAttribute
    public Rider get(@RequestParam(required = false) String id) {
        Rider entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = riderService.get(id);
        }
        if (entity == null) {
            entity = new Rider();
        }
        return entity;
    }

    @ApiOperation(notes = "appUpdateRiderLngLat", httpMethod = "GET", value = "实时上传骑手经纬度")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "骑手id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "lng", value = "经度", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "lat", value = "纬度", required = true, paramType = "query", dataType = "string"),

    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateRiderLngLat")
    public AjaxJson appUpdateRiderLngLat(Rider rider) {
        AjaxJson j = new AjaxJson();
        riderService.save(rider);
        j.setSuccess(true);
        j.setErrorCode("666");
        j.setMsg("修改成功");
        return j;
    }

    @ApiOperation(notes = "appUpdateRiderYesNO", httpMethod = "GET", value = "骑手接单/不接单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "骑手id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "yesNO", value = "0:不在线1:在线", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appUpdateRiderYesNO")
    public AjaxJson appGetRideappUpdateRiderYesNOrDetail(Rider rider) {
        AjaxJson j = new AjaxJson();
        riderService.save(rider);
        j.setMsg("修改成功");
        j.setErrorCode("666");
        j.setSuccess(true);
        return j;

    }

    @ApiOperation(notes = "appGetRiderDetail", httpMethod = "GET", value = "获取骑手端信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "骑手id", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetRiderDetail")
    public AjaxJson appGetRiderDetail(Rider rider) {
        AjaxJson j = new AjaxJson();
        if (null == rider) {
            j.setSuccess(false);
            j.setMsg("暂无此骑手数据");
            j.setErrorCode("111");
        } else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data", rider);
        }
        return j;
    }

    @ApiOperation(notes = "appRiderLogin", httpMethod = "POST", value = "骑手端手机号登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appRiderLogin")
    public AjaxJson appRiderLogin(String phone, String code) {
        AjaxJson j = new AjaxJson();
        Rider rider = new Rider();
        rider.setPhone(phone);
        List<Rider> list = riderService.findList(rider);
        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(code)) {
            String storephone = CacheUtils.get("rider" + phone).toString();
            if (code.equals(storephone)) {
                if (list.size() == 0) {
                    //注册
                    rider.setImg("");
                    rider.setIsRen("0");
                    rider.setIsShen("0");
                    rider.setMoney("0");
                    rider.setYesNO("0");
                    rider.setIsYou("0");
                    rider.setName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    riderService.save(rider);
                    j.setSuccess(true);
                    j.setErrorCode("888");
                    j.setMsg("注册成功");
                    j.put("data", rider);
                    /*String token = JwtHelper.createJWT(store.getPhone(), store.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store.getId() + "_" + store.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                } else if (list.size() == 1) {
                    //登陆
                    j.setMsg("登陆成功");
                    j.setErrorCode("666");
                    j.setSuccess(true);
                    j.put("data", list.get(0));

                    /*String token = JwtHelper.createJWT(store1.getPhone(), store1.getId(), 365 * 24 * 60 * (60 * 1000), "base64security");
                    String tokenRedis = store1.getId() + "_" + store1.getPhone() + "_mhmm_User";
                    CacheUtils.put(tokenRedis, token);
                    j.put("token",token);*/
                } else {
                    j.setMsg("登陆失败,账号异常");
                    j.setErrorCode("777");
                    j.setSuccess(false);
                }
            } else {
                j.setMsg("验证码错误");
                j.setErrorCode("777");
                j.setSuccess(false);
            }

        } else {
            j.setMsg("登陆失败,账号异常");
            j.setErrorCode("777");
            j.setSuccess(false);
        }
        return j;
    }

    @ApiOperation(notes = "appRiderSanLogin", httpMethod = "POST", value = "骑手端第三方登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1:微信2:qq", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "wei_openid", value = "微信openid", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "qq_id", value = "qqopenid", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "img", value = "头像", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appRiderSanLogin")
    public AjaxJson appRiderSanLogin(String type, String wei_openid, String qq_id, String name, String img) {
        AjaxJson j = new AjaxJson();

        if (type.equals("1")) {
            //微信
            Rider rider = new Rider();
            rider.setWeiOpenid(wei_openid);
            List<Rider> list = riderService.findList(rider);
            if (list.size() == 0) {
                //注册
                rider.setName(name);
                rider.setImg(img);
                rider.setIsShen("0");
                rider.setYesNO("0");
                rider.setIsRen("0");
                rider.setMoney("0");
                rider.setPhone("");
                rider.setStarNum("0");
                rider.setIsYou("0");
                riderService.save(rider);
                j.setSuccess(true);
                j.setErrorCode("888");
                j.setMsg("注册成功");
                j.put("data", rider);
            } else {
                //登陆
                j.setMsg("登陆成功");
                j.setErrorCode("666");
                j.setSuccess(true);
                j.put("data", list.get(0));
            }
        } else if (type.equals("2")) {
            //qq

            Rider rider = new Rider();
            rider.setQqId(qq_id);
            List<Rider> list = riderService.findList(rider);
            if (list.size() == 0) {
                //注册
                rider.setName(name);
                rider.setImg(img);
                rider.setIsShen("0");
                rider.setIsRen("0");
                rider.setMoney("0");
                rider.setYesNO("0");
                rider.setPhone("");
                rider.setStarNum("0");
                rider.setIsYou("0");
                rider.setIsUser("2");
                riderService.save(rider);
                j.setSuccess(true);
                j.setErrorCode("888");
                j.setMsg("注册成功");
                j.put("data", rider);
            } else {
                //登陆
                j.setMsg("登陆成功");
                j.setErrorCode("666");
                j.setSuccess(true);
                j.put("data", list.get(0));
            }

        }
        return j;
    }


    @ApiOperation(notes = "appRiderBangPhone", httpMethod = "POST", value = "骑手端手机号绑定")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "id", value = "骑手id", required = true, paramType = "query", dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appRiderBangPhone")
    public AjaxJson appRiderBangPhone(String phone, Rider rider) {
        AjaxJson j = new AjaxJson();
        Rider rider1 = new Rider();
        rider1.setPhone(phone);
        List<Rider> list = riderService.findList(rider1);
        if (list.size() > 0) {
            j.setErrorCode("111");
            j.setSuccess(false);
            j.setMsg("手机号已存在");
        } else {
            rider.setPhone(phone);
            riderService.save(rider);
            j.setMsg("绑定成功");
            j.setSuccess(true);
            j.setErrorCode("666");
        }

        return j;
    }


    @ApiOperation(notes = "appRiderRen", httpMethod = "POST", value = "骑手端认证资料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "骑手id", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "骑手名称", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "card", value = "身份证号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "cardZ", value = "身份证正面", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "cardF", value = "身份证反面", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "cardS", value = "身份证手持", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "img", value = "头像（修改资料才传）", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "name", value = "名称（修改资料才传）", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "phone", value = "手机号码（修改资料才传）", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "type", value = "1:认证资料2:修改资料", required = true, paramType = "query", dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/appRiderRen")
    public AjaxJson appRiderRen(Rider rider, HttpServletRequest request) {

        AjaxJson j = new AjaxJson();

        String type = request.getParameter("type");
        if (type.equals("1")) {
            rider.setIsRen("1");
            riderService.save(rider);

            j.setMsg("认证成功");

            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data", rider);
        } else if (type.equals("2")) {

            String phone = request.getParameter("phone");
            String id = request.getParameter("id");
            Rider rider2 = riderService.get(id);
            //判断传过来的手机是否和原手机号相同
            if (!rider2.getPhone().equals(phone)) {
                Rider rider1 = new Rider();
                rider1.setPhone(phone);
                List<Rider> list = riderService.findList(rider1);
                if (list.size() > 1) {
                    j.setErrorCode("777");
                    j.setSuccess(false);
                    j.setMsg("此手机号已经存在!");
                }
            }
            rider.setIsShen("0");
            riderService.save(rider);
            j.setMsg("修改成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data", rider);
        }
        return j;
    }


}
