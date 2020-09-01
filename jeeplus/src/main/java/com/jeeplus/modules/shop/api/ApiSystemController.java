package com.jeeplus.modules.shop.api;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.CacheUtils;
import com.jeeplus.core.web.BaseController;

import com.jeeplus.modules.shop.config.MyStaticMap;
import com.jeeplus.modules.shop.express.entity.Express;
import com.jeeplus.modules.shop.express.service.ExpressService;
import com.jeeplus.modules.shop.systemnotice.entity.SystemNotice;
import com.jeeplus.modules.shop.systemnotice.service.SystemNoticeService;
import com.jeeplus.modules.shop.systemtype.entity.SystemType;
import com.jeeplus.modules.shop.systemtype.service.SystemTypeService;
import com.jeeplus.modules.shop.systemyin.entity.SystemYin;
import com.jeeplus.modules.shop.systemyin.service.SystemYinService;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author lhh
 * @date 2019/11/27 0027
 */

@Api(value="ApiSystemController",description="App公用控制器/隐私政策/验证码/系统消息")
@Controller
@CrossOrigin
@RequestMapping(value = "${adminPath}/api/store")
public class ApiSystemController extends BaseController {

    @Autowired
    private SystemYinService systemYinService;
    @Autowired
    private SystemNoticeService systemNoticeService;
    @Autowired
    private SystemTypeService systemTypeService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SystemService systemService;



    @ApiOperation(notes = "sendSms", httpMethod = "GET", value = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value = "手机号码",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "状态0用户1商家2骑手",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/sendSms")
    public AjaxJson sendSms(String phone,String status) throws  Exception{
        AjaxJson j = new AjaxJson();

        int mobile_code = (int)((Math.random()*9+1)*100000);
        CacheUtils.put(phone,mobile_code);
        if("0".equals(status)){
            //用户
            CacheUtils.put("customer"+phone,mobile_code);
        }else if("1".equals(status)){
            //商家
            CacheUtils.put("store"+phone,mobile_code);
        }else{
            //骑手
            CacheUtils.put("rider"+phone,mobile_code);
        }
        j.setMsg("获取成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        j.put("data",mobile_code);
        return j;
    }




    @ApiOperation(notes = "getSystemNotice", httpMethod = "GET", value = "获取系统通知")
    @ApiImplicitParams({
    })
    @ResponseBody
    @RequestMapping(value = "/getSystemNotice")
    public AjaxJson getSystemNotice(){
        AjaxJson j = new AjaxJson();

        List<SystemNotice> list=systemNoticeService.findList(new SystemNotice());
        if(list.size() == 0){
            j.setMsg("暂无数据");
            j.setSuccess(false);
            j.setErrorCode("111");
        }else {
            j.setMsg("查询成功");
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",list);
        }
        return j;
    }



    @ApiOperation(notes = "getSystemNoticeDetail", httpMethod = "GET", value = "根据id获取系统通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name="notice_id",value = "通知id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/getSystemNoticeDetail")
    public AjaxJson getSystemNoticeDetail(String notice_id){
        AjaxJson j = new AjaxJson();

        if(notice_id!=""&& null ==notice_id){
            j.setErrorCode("777");
            j.setSuccess(false);
            j.setMsg("参数错误");
        }
        SystemNotice systemNotice=systemNoticeService.get(notice_id);
        String content=systemNotice.getContent();
        StringEscapeUtils.unescapeHtml4(content);
        systemNotice.setContent(content);

        j.setMsg("查询成功");
        j.setSuccess(true);
        j.setErrorCode("666");
        j.put("data",systemNotice);
        return j;
    }


    @ApiOperation(notes = "getExpress", httpMethod = "GET", value = "获取快递方式")
    @ApiImplicitParams({
    })
    @ResponseBody
    @RequestMapping(value = "/getExpress")
    public AjaxJson getExpress(){
        AjaxJson j=new AjaxJson();

        Express express=new Express();

        List<Express> list=expressService.findList(express);
        if(list.size() == 0){
            j.setMsg("暂无快递数据");
            j.setSuccess(false);
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",list);
        }
        return j;
    }
    @ApiOperation(notes = "appGetSystemYin", httpMethod = "GET", value = "获取隐私政策")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value = "1:用户2:商家3:骑手",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/appGetSystemYin")
    public AjaxJson appGetSystemYin(String type){
        AjaxJson j=new AjaxJson();


        SystemYin systemYin=new SystemYin();
        systemYin.setType(type);
        List<SystemYin> list=systemYinService.findList(systemYin);

        if(list.size() == 0){
            j.setMsg("暂无数据");
            j.setSuccess(false);
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setErrorCode("666");
            SystemYin systemYin1=list.get(0);
            systemYin1.setContent(StringEscapeUtils.unescapeHtml4(systemYin1.getContent()));
            j.put("data",systemYin1);
            j.setMsg("查询成功");
        }
        return j;
    }




    @ApiOperation(notes = "appGetSystemType", httpMethod = "GET", value = "获取平台分类")
    @ApiImplicitParams({
    })
    @ResponseBody
    @RequestMapping(value = "/appGetSystemType")
    public AjaxJson appGetSystemType(){
        AjaxJson j=new AjaxJson();



        List<SystemType> list=systemTypeService.findList(new SystemType());

        if(list.size() == 0){
            j.setMsg("暂无数据");
            j.setSuccess(false);
            j.setErrorCode("111");
        }else {
            j.setSuccess(true);
            j.setErrorCode("666");
            j.put("data",list);
            j.setMsg("查询成功");
        }
        return j;
    }

    @ApiOperation(notes = "appGetServiceId", httpMethod = "POST", value = "随机获取客服id")
    @ApiImplicitParams({
    })
    @ResponseBody
    @RequestMapping(value = "/appGetServiceId")
    public AjaxJson appGetServiceId(){
        AjaxJson j=new AjaxJson();

        List<Office> service = officeService.getService();
        if(service.size()>0){
            int i = new Random().nextInt(service.size());
            Office office=service.get(i);
            if(office!=null){
                List<String> loginNames=systemService.getRandomUser(office.getId());
                int loginsize = new Random().nextInt(service.size());
                j.put("data",loginNames.get(loginsize));
                j.setSuccess(true);

            }else{
                j.setMsg("暂无客服");
                j.setSuccess(false);
            }
        }else{
            j.setMsg("暂无客服部门");
            j.setSuccess(false);
        }
        return j;
    }
    @ApiOperation(notes = "appGetServiceChatRecord", httpMethod = "POST", value = "查询我和客服之间的聊天")
    @ApiImplicitParams({
    })
    @ResponseBody
    @RequestMapping(value = "/appGetServiceChatRecord")
    public AjaxJson appGetServiceChatRecord(){
        AjaxJson j=new AjaxJson();

        List<Office> service = officeService.getService();
        if(service.size()>0){
            int i = new Random().nextInt(service.size());
            Office office=service.get(i);
            if(office!=null){
                List<String> loginNames=systemService.getRandomUser(office.getId());
                int loginsize = new Random().nextInt(service.size());
                j.put("data",loginNames.get(loginsize));
                j.setSuccess(true);

            }else{
                j.setMsg("暂无客服");
                j.setSuccess(false);
            }
        }else{
            j.setMsg("暂无客服部门");
            j.setSuccess(false);
        }
        return j;
    }


}
