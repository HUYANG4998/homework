package com.jeeplus.modules.shop.api.deal.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.shop.api.chat.service.ChatService;
import com.jeeplus.modules.shop.api.deal.entity.Deal;
import com.jeeplus.modules.shop.api.deal.entity.DealSon;
import com.jeeplus.modules.shop.api.deal.service.DealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.List;
@Api(value="DealController",description="交易动态")
@Controller
@RequestMapping(value = "${adminPath}/api/deal")
@CrossOrigin
public class DealController {
    @Autowired
    private DealService dealService;


    /**查看关于我的所有交易动态*/
    @ApiImplicitParams({
            @ApiImplicitParam(name="userid",value = "用户id",required = true, paramType = "query",dataType = "string"),
    })
    @ResponseBody
    @RequestMapping(value = "/selectDeal")
    public AjaxJson selectDeal(String userid){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(userid)){
            List<DealSon> dealSonlList=dealService.selectDeal(userid);
            j.setSuccess(true);
            j.setMsg("查询成功");
            j.setErrorCode("666");
            j.put("data",dealSonlList);
        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }
    @ResponseBody
    @RequestMapping(value = "/updateDeal")
    public AjaxJson updateDeal(String dealId){
        AjaxJson j=new AjaxJson();
        if(!StringUtils.isEmpty(dealId)){
            Deal deal=dealService.selectDealById(dealId);
            if(!"1".equals(deal.getStatus())){
                dealService.updateByDealId(dealId);
                j.setSuccess(true);
                j.setMsg("修改成功");
            }else{
                j.setSuccess(false);
                j.setMsg("状态已读");
            }

        }else{
            j.setSuccess(false);
            j.setMsg("参数错误");
            j.setErrorCode("111");
        }
        return j;
    }
}
