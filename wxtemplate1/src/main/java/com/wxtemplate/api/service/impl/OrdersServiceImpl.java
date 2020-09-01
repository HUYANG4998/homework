package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.*;
import com.wxtemplate.api.mapper.*;
import com.wxtemplate.api.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.service.IUserService;
import com.wxtemplate.api.util.PushtoSingle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-17
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderinfoMapper orderinfoMapper;
    @Resource
    private IUserService userService;
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private CarMapper carMapper;
    @Resource
    private OrderinfoMapper orderInfoMapper;
    @Override
    public List<Orders> takeOrdersHall(String userid) {
        List<Orders> ordersList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            ordersList=ordersMapper.selectOrderList(userid);

        }
        return ordersList;
    }

    @Override
    public List<Map<String,Object>> selectAllOrder() {
        List<Map<String,Object>> listMap=ordersMapper.selectAllOrder();
        listMap.parallelStream().forEach((map)->{
            if(map.containsKey("status")){
                String status=map.get("status").toString();
                if("0".equals(status)){
                    map.put("status","待审核");
                }else if("1".equals(status)){
                    map.put("status","已通过");
                }else{
                    map.put("status","已驳回");
                }
            }
            if(map.containsKey("hoteladress")){
                String hoteladress=map.get("hoteladress").toString();
                if("null".equals(hoteladress)){
                    map.put("hoteladress","无");
                }
            }
        });
        return listMap;
    }

    @Override
    public Map<String, Object> selectOrderByOrderid(String orderid,String type) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(type)){
            map=ordersMapper.selectOrdersByOrderId(orderid);
            if(map!=null&&!map.isEmpty()){
                map.put("carlist",userService.getorderinfo(orderid,type));
            }
        }
        return map;
    }

    @Override
    public void orderAudit(Map<String, Object> result) {
        if(!result.isEmpty()){
            String status=result.get("status") == null?null:result.get("status").toString();
            String orderid=result.get("orderid") == null?null:result.get("orderid").toString();
            if(!StringUtils.isEmpty(status)){
                Orders order = ordersMapper.selectById(orderid);
                if(order!=null){
                    if("2".equals(status)){
                        String cause=result.get("cause") == null?null:result.get("cause").toString();
                        User user=userMapper.selectById(order.getUserid());
                        order.setReason(cause);
                        ordersMapper.deleteById(order);
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition("订单已驳回");
                        notice.setContent("订单已驳回，请重新提交订单");
                        notice.setIsread("0");
                        notice.setUserid(order.getUserid());
                        noticeMapper.insert(notice);
                        if(!StringUtils.isEmpty(user.getCid())){
                            Map<String,Object> r=new HashMap<>();
                            r.put("cid",user.getCid());
                            r.put("title",notice.getAddition());
                            r.put("text",notice.getContent());
                            r.put("version", user.getVersion());
                            PushtoSingle.push(r);
                        }
                    }else{
                        order.setStatus(status);
                        ordersMapper.updateById(order);
                        Orders orders=ordersMapper.selectById(orderid);
                        List<User> userList=userMapper.selectUserAll();
                        for (User u:userList){
                            Notice notice=new Notice();
                            notice.setAddtime(DateUtil.now());
                            notice.setAddition("开始抢单啦");
                            StringBuffer sb=new StringBuffer();
                            sb.append("用户***"+"急需主婚车"+orders.getMaincartype()+"1台");
                            if(!StringUtils.isEmpty(orders.getDeputycartype())){
                                sb.append("，副车"+orders.getDeputycartype()+orders.getDeputycarnum()+"台");
                            }
                            sb.append("，请尽快抢单！");
                            notice.setContent(sb.toString());

                            User infoUser=userMapper.selectById(u.getUserid());
                            if(!StringUtils.isEmpty(infoUser.getCid())){
                                Map<String,Object> r=new HashMap<>();
                                r.put("cid",infoUser.getCid());
                                r.put("title",notice.getAddition());
                                r.put("text",notice.getContent());
                                r.put("version", infoUser.getVersion());
                                PushtoSingle.push(r);
                            }

                            notice.setIsread("0");
                            notice.setUserid(u.getUserid());
                            noticeMapper.insert(notice);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Map<String,Object>> selectOrderSingle(String type,String ordernumber) {
        List<Map<String,Object>> orderList=new ArrayList<>();
        if(!StringUtils.isEmpty(type)){
            orderList=ordersMapper.selectOrdersByType(type,ordernumber);
            orderList.parallelStream().forEach((order)->{
                if(order.containsKey("notice")){
                    String notice=order.get("notice")==null?null:order.get("notice").toString();
                    if("1".equals(notice)){
                        order.put("notice","已通知");
                    }else if("2".equals(notice)){
                        order.put("notice","待通知");
                    }else{
                        order.put("notice","个人");
                    }
                }else{
                    order.put("notice","个人");
                }
                if("3".equals(type)){
                    if(order.containsKey("maincartype")||order.containsKey("deputycartype")){
                        order.put("notice","个人");
                    }else{
                        order.put("notice","平台");
                    }
                }

            });
        }
        return orderList;
    }

    @Override
    public void orderSingleAudit(String orderid, String remark) {
        if(!StringUtils.isEmpty(orderid)){
            Orders order=new Orders();
            order.setOrderid(orderid);
            if(!StringUtils.isEmpty(remark)){
                order.setRemark(remark);
                ordersMapper.updateById(order);
            }

            /**查询归属于这个订单的预定子订单*/
            List<Orderinfo> orderinfoList=orderinfoMapper.selectOrderinfoByOrderid(orderid);
            /**遍历所有子订单进行状态变更*/
            orderinfoList.parallelStream().forEach((orderinfo)->{
                orderinfo.setType("2");
                orderinfoMapper.updateById(orderinfo);
            });
            Orders orderOne=ordersMapper.selectById(orderid);
            if(!StringUtils.isEmpty(orderOne.getMaincartype())||!StringUtils.isEmpty(orderOne.getDeputycartype())){
                Map<String,Object> map=ordersMapper.selectMainOrderInfoByNum(1,orderid);
                String main=map.get("mainRemain").toString();
                if("0".equals(main)&&!org.springframework.util.StringUtils.isEmpty(main)){
                    map=ordersMapper.selectDeputyOrderInfoByNum(orderid);
                    String deputy=map.get("deputyRemain").toString();
                    if("0".equals(deputy)&&!org.springframework.util.StringUtils.isEmpty(deputy)){
                        /**将大订单状态更改为进行中*/
                        order.setOrderstatus("1");
                        ordersMapper.updateById(order);
                    }
                }
            }else{
                order.setOrderstatus("1");
                ordersMapper.updateById(order);
            }
        }
    }

    @Override
    public void updateOrdersRemark(Orders order) {
        if(order!=null){

            ordersMapper.updateById(order);
        }
    }

    @Override
    public Map<String, Object> selectOrderNumber(String userid) {
        Map<String,Object> result=new HashMap<>();
        //我发布的
        Map<String,Object> release=new HashMap<>();
        Integer release_zero=ordersMapper.selectByOrderstatus("0",userid);
        Integer release_one=ordersMapper.selectByOrderstatus("1",userid);
        Integer release_two=ordersMapper.selectByOrderstatus("2",userid);
        release.put("zero",release_zero);
        release.put("one",release_one);
        release.put("two",release_two);
        //我接到的
        Map<String,Object> received=new HashMap<>();
        Integer received_zero =orderinfoMapper.selectReceived("0","0",userid);
        Integer received_one =orderinfoMapper.selectReceived("0","1",userid);
        Integer received_two =orderinfoMapper.selectReceived("0","2",userid);
        Integer received_three =orderinfoMapper.selectReceived("0","3",userid);
        received.put("zero",received_zero);
        received.put("one",received_one);
        received.put("two",received_two);
        received.put("three",received_three);
        //我预定的
        Map<String,Object> reservation=new HashMap<>();

        Integer reservation_zero =orderinfoMapper.selectReservation("1","0",userid);
        Integer reservation_one =orderinfoMapper.selectReservation("1","1",userid);
        Integer reservation_two =orderinfoMapper.selectReservation("1","2",userid);
        Integer reservation_three =orderinfoMapper.selectReservation("1","3",userid);
        reservation.put("zero",reservation_zero);
        reservation.put("one",reservation_one);
        reservation.put("two",reservation_two);
        reservation.put("three",reservation_three);
        result.put("release",release);
        result.put("received",received);
        result.put("reservation",reservation);
        return result;
    }

    @Override
    public void ordercancel(String orderid) {
        if(!StringUtils.isEmpty(orderid)){
            Orders order=ordersMapper.selectById(orderid);
            order.setOrderstatus("0");
            List<Orderinfo> orderinfoList=orderinfoMapper.selectOrderinfoByOrderid(orderid);
            for (Orderinfo orderinfo:orderinfoList){
                orderinfo.setType("0");
                orderinfoMapper.updateById(orderinfo);
            }
            ordersMapper.updateById(order);
            Earn earn=new Earn("取消订单",order.getAllprice(),order.getUserid(),"0", DateUtil.now());
            earnMapper.insert(earn);
            User user=userService.selectUser(order.getUserid());
            user.setPrice(user.getPrice()+order.getAllprice());
            userService.updateById(user);
        }
    }

    @Override
    public String orderDeleteOrBank(String orderid) {

        if (!StringUtils.isEmpty(orderid)) {
            String success = "修改成功";
            Orders orders = ordersMapper.selectById(orderid);
            List<Orderinfo> orderinfoList = orderinfoMapper.selectOrderInfoByOrderIdAndStatus(orderid, "0");
            if (!("0".equals(orders.getStatus()))) {

                for (Orderinfo orderinfo : orderinfoList) {
                    String orderpaystatus = orderinfo.getOrderpaystatus();
                    String carmainpaystatus = orderinfo.getCarmainpaystatus();
                    if (!("0".equals(orderpaystatus) && "0".equals(carmainpaystatus))) {
                        success = "用户与车主有交易，无法退回";
                        return success;
                    }
                }
                for (Orderinfo orderinfo : orderinfoList) {
                    Car carMargin=carMapper.selectById(orderinfo.getCarid());
                    carMargin.setStarttime(DateUtil.now());
                    carMargin.setEndtime(DateUtil.now());
                    carMapper.updateById(carMargin);

                    orderInfoMapper.deleteById(orderinfo.getOrderinfoid());

                }
                orders.setStatus("0");
                ordersMapper.updateById(orders);
            } else {
                orders.setOrderstatus("6");
                ordersMapper.updateById(orders);
                for (Orderinfo orderinfo : orderinfoList) {
                    Car carMargin=carMapper.selectById(orderinfo.getCarid());
                    carMargin.setStarttime(DateUtil.now());
                    carMargin.setEndtime(DateUtil.now());
                    carMapper.updateById(carMargin);
                }
                User user=userMapper.selectById(orders.getUserid());
                Notice notice=new Notice();
                notice.setAddtime(DateUtil.now());
                notice.setAddition("订单已驳回");
                notice.setContent("订单已驳回，请重新提交订单");
                notice.setIsread("0");
                notice.setUserid(orders.getUserid());
                noticeMapper.insert(notice);
                if(!StringUtils.isEmpty(user.getCid())){
                    Map<String,Object> r=new HashMap<>();
                    r.put("cid",user.getCid());
                    r.put("title",notice.getAddition());
                    r.put("text",notice.getContent());
                    r.put("version", user.getVersion());
                    PushtoSingle.push(r);
                }

            }
            return success;
        }else{
            return "参数错误";
        }
    }
            /*User user=userMapper.selectById(orders.getUserid());*/
            /*List<Orderinfo> orderinfoList=orderinfoMapper.selectOrderInfoByOrderIdAndStatus(orderid,"0");*/
            /*if("0".equals(orders.getStatus())){
                //删除
                for (Orderinfo orderinfo:orderinfoList){
                    User infoUser=userMapper.selectById(orderinfo.getUserid());
                    if("1".equals(orderinfo.getOrderpaystatus())){
                        //退回用户的钱
                        user.setPrice(user.getPrice()+orderinfo.getPrice());
                        Earn earn=new Earn("平台退回",orderinfo.getPrice(),user.getUserid(),"0",DateUtil.now());
                        earnMapper.insert(earn);
                        Notice notice=new Notice();
                        notice.setIsread("0");
                        notice.setAddition("平台退回订单");
                        notice.setContent("平台退回订单，查看钱包资金是否正确");
                        notice.setUserid(user.getUserid());
                        notice.setAddtime(DateUtil.now());
                        noticeMapper.insert(notice);
                    }
                    if("1".equals(orderinfo.getCarmainpaystatus())){
                        //退回车主的钱
                        infoUser.setPrice(infoUser.getPrice()+orderinfo.getMargin());
                        userMapper.updateById(infoUser);
                        Earn earn=new Earn("平台退回",Double.valueOf(orderinfo.getMargin()),infoUser.getUserid(),"0",DateUtil.now());
                        earnMapper.insert(earn);
                        Notice notice=new Notice();
                        notice.setIsread("0");
                        notice.setAddition("平台退回订单");
                        notice.setContent("平台退回订单，查看钱包资金是否正确");
                        notice.setUserid(infoUser.getUserid());
                        notice.setAddtime(DateUtil.now());
                        noticeMapper.insert(notice);
                    }
                    orderinfo.setType("6");
                    orderinfoMapper.updateById(orderinfo);
                }
                userMapper.updateById(user);
                orders.setOrderstatus("6");
            }else{
                //退回
                for (Orderinfo orderinfo:orderinfoList){
                    String orderpaystatus = orderinfo.getOrderpaystatus();
                    String carmainpaystatus = orderinfo.getCarmainpaystatus();
                    if(!("0".equals(orderpaystatus)&&"0".equals(carmainpaystatus))){
                        success="用户与车主有交易，无法退回";
                        return success;
                    }
                }
                orders.setStatus("0");
                ordersMapper.updateById(orders);
            }*/






    @Override
    public List<Map<String,Object>> selectOrderComplete(String orderstatus,String ordernumber) {
        List<Map<String,Object>> listOrder=new ArrayList<>();
        listOrder=ordersMapper.selectOrderComplete(orderstatus,ordernumber);
        if(listOrder.size()>0){
            listOrder.parallelStream().forEach((order)->{
                if(order.containsKey("notice")){
                    String notice=order.get("notice")==null?null:order.get("notice").toString();
                    if("1".equals(notice)){
                        order.put("notice","已通知");
                    }else if("2".equals(notice)){
                        order.put("notice","待通知");
                    }else{
                        order.put("notice","个人");
                    }
                }else{
                    order.put("notice","个人");
                }

            });
        }

        return listOrder;
    }
}
