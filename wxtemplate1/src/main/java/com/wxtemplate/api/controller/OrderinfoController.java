package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Orderinfo;
import com.wxtemplate.api.entity.Orders;
import com.wxtemplate.api.service.IOrderinfoService;
import com.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单详情 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@RestController
@RequestMapping("/api/orderinfo")
public class OrderinfoController {


    @Autowired
    private IOrderinfoService orderinfoService;
    /**
     * 后台根据订单id查询个人订单那已完成的
     */

    @PostMapping(value = "/selectOrderinfoByOrderid")
    public Result selectOrderComplete(String orderid,String type){
        try {
            if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(type)){
                List<Map<String,Object>> orderinfoList=orderinfoService.selectOrderinfoByOrderid(orderid,type);

                return Result.success(orderinfoList);

            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateOrdersRemark fail");
        }
    }
    /**
     * 后台 根据orderinfoid查询订单和车辆信息
     */
    @PostMapping(value = "/selectOrderinfoByOrderinfoid")
    public Result selectOrderinfoByOrderinfoid(String orderinfoid){
        try {
            if(!StringUtils.isEmpty(orderinfoid)){
                Map<String,Object> orderinfoMap=orderinfoService.selectOrderinfoByOrderinfoid(orderinfoid);
                if(orderinfoMap!=null&&!orderinfoMap.isEmpty()){
                    return Result.success(orderinfoMap);
                }else{
                    return Result.fail("检索失败");
                }
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrderinfoByOrderinfoid fail");
        }
    }
    /**
     * 后台 出现卫生问题或其他问题 需要减少佣金 修改子订单
     */
    @PostMapping(value = "/updateOrderinfo")
    public Result updateOrderinfo(Orderinfo orderinfo){
        try {
            if(orderinfo!=null){
                orderinfoService.updateOrderinfo(orderinfo);
                return Result.success("修改成功");
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateOrderinfo fail");
        }
    }
}
