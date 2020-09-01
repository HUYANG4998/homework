package com.jeeplus.modules.shop.api.chat.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import com.jeeplus.modules.shop.api.chat.service.ChatService;
import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.util.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
@Api(value="ChatController",description="App消息端")
@RestController
@RequestMapping(value = "${adminPath}/api/chat")
@CrossOrigin
public class ChatController {
    @Autowired
    private ChatService chatService;

    @ApiOperation(notes = "selectMessage", httpMethod = "POST", value = "获取单方用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",required = true, paramType = "query",dataType = "string"),
            @ApiImplicitParam(name="status",value = "0用户1商家2骑手",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/selectMessage")
    public AjaxJson selectMessage(String userid,String status){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(userid)&&!StringUtils.isEmpty(status)){
            List<Map<String,Object>> listMap=chatService.selectByUseridAndStatus(userid,status);
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",listMap);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }
    /**
     * 用户获取骑手和商家信息*/
    @ApiOperation(notes = "selectStoreAndRider", httpMethod = "POST", value = "获取双方消息信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/selectStoreAndRider")
    public AjaxJson selectMessage(String userid){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(userid)){

            List<Map<String,Object>> listMap=chatService.selectByUseridAndStatus(userid,"1");
            List<Map<String,Object>> listMap1=chatService.selectByUseridAndStatus(userid,"2");
            listMap.addAll(listMap1);
            Sort.ListSort(listMap);
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",listMap);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }

    /**查询单个聊天信息*/
    @ApiOperation(notes = "selectMessageByFormAndTo", httpMethod = "POST", value = "查询单个聊天信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "我的id",required = true, paramType = "query",dataType = "json"),
            @ApiImplicitParam(name="oppositeid",value = "对方id",required = true, paramType = "query",dataType = "json"),
            @ApiImplicitParam(name="wostatus",value = "我的状态0用户1商家2骑手",required = true, paramType = "query",dataType = "json"),
            @ApiImplicitParam(name="nistatus",value = "对方状态0用户1商家2骑手",required = true, paramType = "query",dataType = "json"),
            @ApiImplicitParam(name="number",value = "当前消息数量",required = true, paramType = "query",dataType = "json")
    })
    @ResponseBody
    @RequestMapping(value = "/selectMessageByFormAndTo")
    public AjaxJson selectMessageByFormAndTo(@RequestBody Map<String,Object> map){
        AjaxJson j=new AjaxJson();
        if(map!=null&&!map.isEmpty()){
            Map<String,Object> result=chatService.selectMessageByFormAndTo(map);
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",result);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }
    /**删除一个人的消息*/
    @ApiOperation(notes = "deleteMessage", httpMethod = "POST", value = "删除一个人的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="chatid",value = "聊天id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/deleteMessage")
    public AjaxJson deleteMessage(String chatid){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(chatid)){
            Chat chat=chatService.selectMessageById(chatid);
            if("1".equals(chat.getDelFlag())){

                chatService.updateByChatId(chat);
                j.setSuccess(true);
                j.setMsg("修改成功");
            }else{
                j.setSuccess(false);
                j.setMsg("消息已删除");
            }

        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }

    /**
     * 查询消息数量
     * @param userid
     * @return
     */
    @ApiOperation(notes = "selectMessageNumber", httpMethod = "POST", value = "查询当前消息数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",required = true, paramType = "query",dataType = "string")
    })
    @ResponseBody
    @RequestMapping(value = "/selectMessageNumber")
    public AjaxJson selectMessageNumber(String userid){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(userid)){
            Integer number=chatService.selectMessageNumber(userid);
            j.setSuccess(true);
            j.setMsg("修改成功");
            j.setErrorCode("666");
            j.put("number",number);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }
    @PostMapping(value = "/select")
    @NoRepeatSubmit(time = 2)
    @ResponseBody
    @ApiOperation(value="测试重复提交")
    public Customer select(){
        AjaxJson j=new AjaxJson();
        Customer customer=new Customer();
        customer.setImg("sdf");

        customer.setIsUser(null);
        j.setSuccess(false);

        j.put("customer",customer);
        Map<String,Object> map=new HashMap<>();
        map.put("customer",customer);
        String sdf=null;
        return customer;
    }


}
