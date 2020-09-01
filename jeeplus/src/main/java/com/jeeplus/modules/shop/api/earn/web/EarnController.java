package com.jeeplus.modules.shop.api.earn.web;


import com.alibaba.druid.util.StringUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.modules.shop.api.earn.entity.Earn;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收益表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-20
 */
@Api(value="EarnController",description="收益明细")
@RestController
@RequestMapping("${adminPath}/api/earn")
@CrossOrigin
public class EarnController {

    @Autowired
    private EarnService earnService;

    /**
     * 收益明细   根据时间
     */
    @ApiOperation(notes = "selectEarnByAddtime", httpMethod = "POST", value = "获取带有时间的明细收益")
    @ApiImplicitParams({
            @ApiImplicitParam(name="addtime",value = "时间",required = true, paramType = "query",dataType = "string"),
    })
    @PostMapping(value = "/selectEarnByAddtime")
    public AjaxJson selectEarnByAddtime(String addtime, HttpServletRequest request){
        /*String userid=(String)request.getAttribute("userid");*/
        String userid="b023dcf1f54c46dfa22955e954c706ff";
        AjaxJson j=new AjaxJson();

        try {
            if(!StringUtils.isEmpty(userid)){
                List<Earn> earnList=earnService.selectEarnByAddtime(userid,addtime);
                j.setSuccess(true);
                j.setMsg("查询成功");
                j.put("data",earnList);

            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("收益明细异常");
        }
       return j;

    }
    @ApiOperation(notes = "selectPriceAndYesterDay", httpMethod = "POST", value = "查询当前余额和昨日收益")
    @ApiImplicitParams({
            @ApiImplicitParam(name="status",value = "1商家2骑手",required = true, paramType = "query",dataType = "string"),
    })
    @PostMapping(value = "/selectPriceAndYesterDay")
    public AjaxJson selectPriceAndYesterDay(String status, HttpServletRequest request){
        /*String userid=(String)request.getAttribute("userid");*/
        String userid="b023dcf1f54c46dfa22955e954c706ff";
        AjaxJson j=new AjaxJson();
        try {
            if(!StringUtils.isEmpty(status)&&!StringUtils.isEmpty(userid)){
                Map<String,Object> map=earnService.selectPriceAndYesterDay(userid,status);
                j.setSuccess(true);
                j.setMsg("查询成功");
                j.put("data",map);

            }else{
                j.setSuccess(false);
                j.setMsg("参数错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("收益明细异常");
        }
        return j;

    }
}
