package com.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Orders;
import com.wxtemplate.api.service.IOrdersService;
import com.wxtemplate.tools.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-17
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    /**
     * 接单大厅  订单查询
     * @return
     */
    @PostMapping(value = "/takeOrdersHall")
    public Result takeOrdersHall(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            List<Orders> carList= ordersService.takeOrdersHall(userid);

            return Result.success(carList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("takeOrdersHall fail");
        }
    }
    /**
     * 后台 查询带审核的全部订单
     */
    @PostMapping(value = "/selectAllOrder")
    public Result selectAllOrder(){
        try {
            List<Map<String,Object>> orderList= ordersService.selectAllOrder();

            return Result.success(orderList);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectAllOrder fail");
        }
    }
    /**
     * 后台 查询单个订单 带审核
     */
    @PostMapping(value = "/selectOrderByOrderid")
    public Result selectOrderByOrderid(String orderid,String type){
        try {
            if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(type)){
                Map<String,Object> orderMap= ordersService.selectOrderByOrderid(orderid,type);
                if(orderMap!=null&&!orderMap.isEmpty()){
                    return Result.success(orderMap);
                }else{
                    return Result.fail("检索无订单");
                }
            }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrderByOrderid fail");
        }
    }
    /**
     * 后台 订单审核
     */
    @PostMapping(value = "/orderAudit")
    public Result orderAudit(@RequestBody Map<String,Object> result){
        try {
            if(!result.isEmpty()){
                ordersService.orderAudit(result);
                return Result.success("审核通过");
            }else{
                return Result.fail("审核失败！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("orderAudit fail");
        }
    }
    /**
     * 后台  派单中 进行中审核查询列表
     */
    @PostMapping(value = "/selectOrderSingle")
    public Result selectOrderSingle(String type,String ordernumber){
        try {

                if(!StringUtils.isEmpty(type)){
                    List<Map<String,Object>> orderList= ordersService.selectOrderSingle(type,ordernumber);
                    return Result.success(orderList);
                }else{
                return Result.fail("条件不足");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrderSingle fail");
        }
    }
    /**
     * 后台  派单中审核通过 到达进行中阶段
     */
    @PostMapping(value = "/orderSingleAudit")
    @Transactional(rollbackFor=Exception.class)
    public Result orderSingleAudit(String orderid,String remark){
        try {
            if(!StringUtils.isEmpty(orderid)){
                ordersService.orderSingleAudit(orderid,remark);
                return Result.success("派单成功");
            }else{
                return Result.fail("派单失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderSingleAudit fail");
        }
    }
    /**
     * 后台  修改订单备注
     */

    @PostMapping(value = "/updateOrdersRemark")
    public Result updateOrdersRemark(Orders order){
        try {
            if(order!=null){
                ordersService.updateOrdersRemark(order);
                return Result.success("审核成功");
            }else{
                return Result.fail("审核失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateOrdersRemark fail");
        }
    }
    /**
     * 后台  查询已完成的全部订单
     */
    @PostMapping(value = "/selectOrderComplete")
    public Result selectOrderComplete(String orderstatus,String ordernumber){
        try {
            if(!StringUtils.isEmpty(orderstatus)){
                List<Map<String,Object>> orderList=ordersService.selectOrderComplete(orderstatus,ordernumber);
                return Result.success(orderList);
            }else{
                return Result.fail("条件不足");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("updateOrdersRemark fail");
        }
    }
    /**
     * 后台  查询已完成的全部订单
     */
    @PostMapping(value = "/selectOrderNumber")
    public Result selectOrderNumber(HttpServletRequest request){
        String userid=(String)request.getAttribute("userid");
        try {
            if(!StringUtils.isEmpty(userid)){
                Map<String,Object> map=ordersService.selectOrderNumber(userid);
                return Result.success(map);
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("selectOrderNumber fail");
        }
    }

    /**
     * 后台  派单中取消订单
     */
    @PostMapping(value = "/ordercancel")
    @Transactional(rollbackFor = Exception.class)
    public Result ordercancel(String orderid){
        try {
            if(!StringUtils.isEmpty(orderid)){
                ordersService.ordercancel(orderid);
                return Result.success("取消成功");
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("ordercancel fail");
        }
    }

    /**
     * 后台  删除订单或通过的退回审核中
     */
    @PostMapping(value = "/orderDeleteOrBank")
    @Transactional(rollbackFor = Exception.class)
    public Result orderDeleteOrBank(String orderId){
        try {
            if(!StringUtils.isEmpty(orderId)){
                String success=ordersService.orderDeleteOrBank(orderId);
                return Result.success(success);
            }else{
                return Result.fail("用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fail("orderDeleteOrBank fail");
        }
    }
}
