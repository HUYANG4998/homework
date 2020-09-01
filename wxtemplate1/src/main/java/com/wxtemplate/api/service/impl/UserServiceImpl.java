package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxtemplate.api.alipay.config.WxPay;
import com.wxtemplate.api.alipay.controller.Pay;
import com.wxtemplate.api.entity.*;
import com.wxtemplate.api.entity.VO.OwnerCertVo;
import com.wxtemplate.api.mapper.*;
import com.wxtemplate.api.util.ConstantUtils;
import com.wxtemplate.api.util.PushtoSingle;
import com.wxtemplate.api.util.RedisUtils;
import com.wxtemplate.api.util.Utils;

import com.wxtemplate.api.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.tools.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RealnameMapper realnameMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ImgsMapper imgsMapper;
    @Resource
    private CarMapper carMapper;
    @Resource
    private LicenseMapper licenseMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderinfoMapper orderInfoMapper;
    @Resource
    private TripMapper tripMapper;
    @Resource
    private GaragMapper garagMapper;
    @Resource
    private EarnMapper earnMapper;
    @Resource
    private CommentuserMapper commentuserMapper;
    @Resource
    private FendbackMapper fendbackMapper;
    @Resource
    private AssetMapper assetMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private CarcolorMapper carcolorMapper;
    @Resource
    private OfficialcarMapper officialcarMapper;
    @Resource
    private EnduserMapper enduserMapper;
    @Resource
    private ChatMapper chatMapper;
    @Resource
    private Pay pay;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private TopupMapper topupMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public User findUser(String userid) {
        User user=null;
        if(!StringUtils.isEmpty(userid)){
            user= userMapper.selectById(userid);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        if(user!=null){
            User u=userMapper.selectById(user.getUserid());
            if(user.getPrice()!=null){
                if(user.getPrice()<u.getPrice()){
                    Topup topup=new Topup();
                    topup.setUserid(u.getUserid());
                    topup.setCreatetime(DateUtil.now());
                    topup.setAddorsub("0");
                    topup.setPrice(String.valueOf(u.getPrice()-user.getPrice()));
                    topupMapper.insert(topup);
                }else if(user.getPrice()>u.getPrice()){
                    Topup topup=new Topup();
                    topup.setUserid(u.getUserid());
                    topup.setCreatetime(DateUtil.now());
                    topup.setAddorsub("1");
                    topup.setPrice(String.valueOf(user.getPrice()-u.getPrice()));
                    topupMapper.insert(topup);
                }
            }
            if("0".equals(user.getIsuser())){
                redisUtils.delete(u.getUserid() + "_" + u.getPhone() + "_xht_User");
            }
            userMapper.updateById(user);

        }
    }

    @Override
    public void authentication(OwnerCertVo vo) {
        String date=DateUtil.now();
        if(vo!=null){
           String userid= vo.getUserid();
           String realname=vo.getRealname();
           String idcard=vo.getIdcard();
           String idcardimages= vo.getIdcardimages();
           if(!StringUtils.isEmpty(userid)){

               /**插入身份证*/
               if(!StringUtils.isEmpty(realname)&&!StringUtils.isEmpty(idcardimages)&&!StringUtils.isEmpty(idcard)){
                   Realname real=realnameMapper.selectRealByUserId(userid);
                   if(real==null){
                       Realname realName=new Realname();
                       realName.setUserid(userid);
                       realName.setRealname(realname);
                       realName.setIdcard(idcard);
                       realName.setAddtime(date);
                       realName.setUpdatetime(date);
                       realName.setRealstatus("0");
                       realnameMapper.insert(realName);
                       Imgs images=new Imgs();
                       images.setObjid(realName.getRealnameid());
                       images.setImgurl(idcardimages);
                       images.setAddtime(date);
                       imgsMapper.insert(images);
                   }else{
                       real.setUserid(userid);
                       real.setRealname(realname);
                       real.setIdcard(idcard);
                       real.setAddtime(date);
                       real.setUpdatetime(date);
                       real.setRealstatus("0");
                       realnameMapper.updateById(real);
                       Imgs imgs=imgsMapper.selectByObjId(real.getRealnameid());
                       imgs.setImgurl(idcardimages);
                       imgsMapper.updateById(imgs);
                   }
               }
           }
        }
    }
    @Override
    public void license(License license) {
        if(license!=null){
            String userid=license.getUserid();
            String licenseurl=license.getLicenseurl();
            String licensenumber=license.getLicensenumber();
            if(!StringUtils.isEmpty(license.getUserid())){
                if(!StringUtils.isEmpty(licenseurl)&&!StringUtils.isEmpty(licensenumber)){
                    License l=licenseMapper.selectLicByUserId(userid);
                    if(l==null){
                        License li=new License();
                        li.setUserid(userid);
                        li.setLicenseurl(licenseurl);
                        li.setLicensenumber(licensenumber);
                        li.setLicensestatus("0");
                        li.setAddtime(DateUtil.now());
                        licenseMapper.insert(li);
                    }else{
                        l.setUserid(userid);
                        l.setLicenseurl(licenseurl);
                        l.setLicensenumber(licensenumber);
                        l.setLicensestatus("0");
                        licenseMapper.updateById(l);
                    }
                }
            }
        }
    }

    @Override
    public void AuthenticationCar(Car car, String carPhotos) {
        if(car!=null&&!StringUtils.isEmpty(carPhotos)){
            Car c=new Car();
            c.setUserid(car.getUserid());
            c.setAddtime(DateUtil.now());
            c.setCarnumber(car.getCarnumber());
            c.setCarmodel(car.getCarmodel());
            c.setCarcolor(car.getCarcolor());
            c.setCardesc(car.getCardesc());
            c.setDrivinglicense(car.getDrivinglicense());
            c.setType(car.getType());
            c.setStatus("0");
            c.setStarttime(DateUtil.now());
            c.setEndtime(DateUtil.now());
            carMapper.insert(c);
            Imgs images=new Imgs();
            images.setImgurl(carPhotos);
            images.setObjid(c.getCarid());
            images.setAddtime(DateUtil.now());
            imgsMapper.insert(images);
        }
    }
    @Override
    public void deleteCar(String carid) {
        if(!StringUtils.isEmpty(carid)){
            carMapper.deleteCarById(carid);
            imgsMapper.deleteByObjId(carid);
        }

    }

    @Override
    public Map<String, Object> getAuthentication(String userid) {
        Map<String,Object> map=new HashMap<String,Object>();
        if(!StringUtils.isEmpty(userid)){

                //个人
               Map<String,Object> realnameMap= realnameMapper.selectRealNameByUserId(userid);

                //企业
                Map<String,Object> licenseMap=licenseMapper.selectLicenseByUserId(userid);
                map.put("realname",realnameMap);
                map.put("license",licenseMap);

        }
        return map;
    }

    @Override
    public void createOrder(Orders order) {
        if(order!=null){
            Orders o=new Orders();
            try {
                BeanUtils.copyProperties(order,o);
            } catch (BeansException e) {
                e.printStackTrace();
            }
            o.setOrdernumber(Utils.getRandomTwelve());
            o.setAddtime(DateUtil.now());
            o.setOrderstatus("0");
            o.setStatus("0");
            ordersMapper.insert(o);
        }
    }


    @Override
    public User findUserByPhone(String phone) {
         User user=null;
        if(!StringUtils.isEmpty(phone)){
           user= userMapper.selectUserByPhone(phone);
        }
        return user;
    }

    @Override
    public void addUser(String phone, String password) {
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(password)){
            User user=new User();
            user.setName("用户"+ Utils.getRandomSix());
            user.setPhone(phone);
            user.setPassword(password);
            user.setAddtime(DateUtil.now());
            user.setUpdatetime(DateUtil.now());
            user.setPrice(0.00);
            user.setHeadurl("http://182.92.64.54:7654/image/0a006476-0d15-42c5-bd8b-27c86a575ecc.png");
            user.setIsuser("1");
            /**个人认证 未写*/
            userMapper.insert(user);
            String userid=enduserMapper.selectRandomOne();
            Chat chat=new Chat();
            chat.setServiceid(userid);
            chat.setUserid(user.getUserid());
            chat.setAddtime(DateUtil.now());
            chatMapper.insert(chat);
        }
    }
    @Override
    public void updateUser(String phone, String newPassword) {
        if(!StringUtils.isEmpty(phone)&&!StringUtils.isEmpty(newPassword)){
            User user=new User();
            user.setPhone(phone);
            user.setPassword(newPassword);
            user.setUpdatetime(DateUtil.now());
            userMapper.updatePasswordByPhone(user);
        }
    }
    @Override
    public Map<String,Object> selectNewOrderDetail(String userid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(userid)){
            Orders orders=ordersMapper.selectNewOrderByUserId(userid);
            if(orders!=null){
                map=ordersMapper.selectMainOrderInfoByNum(1,orders.getOrderid());
                Map<String,Object> map1= ordersMapper.selectDeputyOrderInfoByNum( orders.getOrderid());
                map.put("deputyNum",map1.get("deputyNum"));
                map.put("deputyRemain",map1.get("deputyRemain"));
                map.put("orders",orders);
            }
        }
        return map;
    }

    @Override
    public void addMyOrderInfo(List<Orderinfo> orderinfoList) {
        if(orderinfoList!=null){
            for (Orderinfo orderinfo:orderinfoList ) {
                Orderinfo order=new Orderinfo();
                try {
                    BeanUtils.copyProperties(orderinfo,order);
                } catch (BeansException e) {
                    e.printStackTrace();
                }
                order.setAddtime(DateUtil.now());
                order.setStatus("0");
                order.setType("0");
                /*order.setReviewstatus("0");*/
                order.setOrderpaystatus("0");
                order.setCarmainpaystatus("0");
                order.setTime("1");
                Integer carNumber=orderInfoMapper.selectOrderInfoByOrderIdAndCarId(order.getOrderid(),order.getCarid());
                if(carNumber==0){
                    orderInfoMapper.insert(order);
                }else{
                    continue;
                }

                Orders orders=ordersMapper.selectById(order.getOrderid());
                /**接亲时间*/
                String marryTime=orders.getMarrytime();
                /**小时数*/
                String hour=orders.getHour();
                String endtime=DateUtil.format(DateUtil.offsetHour(DateUtil.parse(marryTime, "yyyy-MM-dd HH:mm:ss"), Integer.valueOf(hour)),"yyyy-MM-dd HH:mm:ss");
                Car car =new Car();
                car.setCarid(order.getCarid());
                car.setStarttime(marryTime);
                car.setEndtime(endtime);
                carMapper.updateById(car);
                User user=userMapper.selectById(orderinfo.getUserid());

                Notice notice=new Notice();
                notice.setAddtime(DateUtil.now());
                notice.setAddition("车主抢单了");
                StringBuffer sb=new StringBuffer();
                if("main".equals(orderinfo.getCartype())){
                    sb.append("车主"+user.getName().subSequence(0,2)+"**抢了主车1台，请尽快支付用车款");
                }else{
                    sb.append("车主"+user.getName().subSequence(0,2)+"**抢了副车1台，请尽快支付用车款");
                }

                notice.setContent(sb.toString());

                User infoUser=userMapper.selectById(orders.getUserid());
                if(!StringUtils.isEmpty(infoUser.getCid())){
                    Map<String,Object> r=new HashMap<>();
                    r.put("cid",infoUser.getCid());
                    r.put("title",notice.getAddition());
                    r.put("text",notice.getContent());
                    r.put("version", infoUser.getVersion());
                    PushtoSingle.push(r);
                }

                notice.setIsread("0");
                notice.setUserid(orders.getUserid());
                noticeMapper.insert(notice);
            }
        }
    }

    @Override
    public Map<String,Object> selectMyOrderInfoDetail(String orderid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(orderid)){
            Orders orders=ordersMapper.selectById(orderid);
            if(orders!=null){
                map=ordersMapper.selectMainOrderInfoByNum(1,orders.getOrderid());
                Map<String,Object> map1= ordersMapper.selectDeputyOrderInfoByNum(orders.getOrderid());
                if(map1!=null){
                    map.put("deputyNum",map1.get("deputyNum"));
                    map.put("deputyRemain",map1.get("deputyRemain"));
                }
                map.put("orders",orders);
            }
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> selectMyOrderCar(String orderid, String userid,String type) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(userid)){
            listMap= carMapper.selectCarByUserIdAndOrderId(userid,orderid,type);
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> order(String orderstatus, String userid) {
        List<Map<String, Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(orderstatus)&&!StringUtils.isEmpty(userid)){
            List<Orders> ordersList=ordersMapper.selectOrderByUserIdAndOrderstatus(userid,orderstatus);
            for (Orders order:ordersList) {
                Map<String,Object> map=new HashMap<>();
                String orderid=order.getOrderid();
                Integer deputycarnum=order.getDeputycarnum();
                if(!StringUtils.isEmpty(orderid)){
                    map=ordersMapper.selectMainOrderInfoByNum(1,orderid);
                    if(!StringUtils.isEmpty(deputycarnum)){
                        Map<String,Object> map1= ordersMapper.selectDeputyOrderInfoByNum( orderid);
                        map.put("deputyNum",map1.get("deputyNum"));
                        map.put("deputyRemain",map1.get("deputyRemain"));
                    }
                    map.put("orders",order);
                }
                listMap.add(map);
            }
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> contactDriver(Map<String,Object> map) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(map!=null&&!map.isEmpty()){
            listMap=carMapper.selectCarByOrderId(map);
        }
        return listMap;
    }

    @Override
    public List<Map<String, Object>> orderViewCarInfo(Map<String,Object> map) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(map!=null&&!map.isEmpty()){
            listMap=carMapper.selectCarByOrderId(map);
            listMap.parallelStream().forEach((info)->{
                if(info.containsKey("xing")){
                    String x=info.get("xing").toString();
                    Integer xing=Double.valueOf(x).intValue();
                    info.put("xing",xing);
                }else{
                    info.put("xing",0);
                }
                Map<String,Object> result=new HashMap<>();
                result.put("orderid",map.get("orderid")==null?null:map.get("orderid").toString());
                result.put("cartype",map.get("cartype")==null?null:map.get("cartype").toString());
                result.put("type",map.get("type")==null?null:map.get("type").toString());
                result.put("userid",info.get("userid")==null?null:info.get("userid").toString());
                info.put("orderinfo",orderInfoMapper.selectOrderinfoByOrderidAndtypeAndCartype(result));
            });
        }
        return listMap;
    }

    @Override
    public List<Trip> selectTrip(String userid) {
        List<Trip> tripList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            tripList= tripMapper.selectList(userid);
        }
        return tripList;
    }

    @Override
    public Trip selectTripByTripId(String tripid) {
        Trip trip=null;
        if(!StringUtils.isEmpty(tripid)){
            trip=tripMapper.selectById(tripid);
        }
        return trip;
    }

    @Override
    public void updateTripByTripId(Trip trip) {
        if(trip!=null){
            tripMapper.updateById(trip);
        }
    }

    @Override
    public void addTrip(Trip trip,String userid) {
        if(trip!=null&&!StringUtils.isEmpty(userid)){
            trip.setTripid(Utils.getUUID());
            trip.setUserid(userid);
            tripMapper.insert(trip);
        }
    }

    @Override
    public void deleteTrip(String tripid) {
        if(!StringUtils.isEmpty(tripid)){
            tripMapper.deleteById(tripid);
        }
    }

    @Override
    public void addGarag(String userid, Map<String,Object> map) {
        if(!StringUtils.isEmpty(userid)&&map!=null&&!map.isEmpty()){
            Garag garag=new Garag();
            garag.setUserid(userid);
            garag.setCarnum(map.get("carnum")==null?null:map.get("carnum").toString());
            garag.setCarcolor(map.get("carcolor")==null?null:map.get("carcolor").toString());
            garag.setCarid(map.get("carid")==null?null:map.get("carid").toString());
            garag.setPackageid(map.get("packageid")==null?null:map.get("packageid").toString());
            garag.setAddtime(DateUtil.now());
            garagMapper.insert(garag);
        }
    }

    @Override
    public void deleteGarag(List<String> garageidList) {
        garageidList.parallelStream().forEach((garageid)->{
            garagMapper.deleteById(garageid);
        });
    }

    @Override
    public List<Map<String,Object>>  selectGarag(String userid) {
        List<Map<String,Object>> garagList=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            garagList=garagMapper.selectByUserId(userid);
        }
        return garagList;
    }

    @Override
    public List<Map<String, Object>> selectOrderByOrderInfo(Map<String, Object> map) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(map!=null){
            String type= map.get("type")==null?null:map.get("type").toString();
            listMap=ordersMapper.selectOrderByOrderInfo(map);
            if(listMap.size()>0){
                listMap.parallelStream().forEach((order)->{
                   String orderid= order.get("orderid")==null?null:order.get("orderid").toString();
                   order.put("carlist",getorderinfo(orderid,type));
                });
            }
        }
        return listMap;
    }

    @Override
    public Map<String, Object> selectOrdersByOrderId(String orderid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(orderid)){
            map=ordersMapper.selectOrdersByOrderId(orderid);


            map.put("carlist",getorderinfo(orderid,null));
        }
        return map;
    }
    public List<Map<String,Object>> getorderinfo(String orderid,String type){
        List<Map<String,Object>> infoMap=new ArrayList<>();
        List<Map<String,Object>> orderinfoList= orderInfoMapper.selectOrderinfoByOrderidAndType(orderid,type);
        orderinfoList.parallelStream().forEach((orderinfo)->{
            String carid= orderinfo.get("carid")==null?null:orderinfo.get("carid").toString();
            String carcolor= orderinfo.get("carcolor")==null?null:orderinfo.get("carcolor").toString();
            String infocartype=orderinfo.get("infocartype")==null?null:orderinfo.get("infocartype").toString();

            Integer number=orderInfoMapper.selectOrderinfoByCaridAndColor(orderid,carid,carcolor,infocartype);
            Map<String,Object> orderMap=new HashMap<>();
            orderMap.put("carid",carid);
            orderMap.put("color",carcolor);
            orderMap.put("infocartype",infocartype);
            orderMap.put("carmodel",orderinfo.get("carmodel")==null?null:orderinfo.get("carmodel").toString());
            orderMap.put("cartype",orderinfo.get("cartype")==null?null:orderinfo.get("cartype").toString());
            orderMap.put("price",orderinfo.get("price")==null?null:Double.valueOf(orderinfo.get("price").toString()));
            orderMap.put("number",number);
            orderMap.put("imgurl",orderinfo.get("imgurl")==null?null:orderinfo.get("imgurl").toString());
            if("main".equals(infocartype)){
                infoMap.add(0,orderMap);
            }else{
                infoMap.add(orderMap);
            }
        });
        return infoMap;
    }

    @Override
    public boolean balancePay(Orders order, List<Map<String, Object>> carList) {
        boolean is_success=false;
        if(order!=null&&carList!=null){
            String maincarid=order.getMaincarid();
            String deputycarid=order.getDeputycarid();
            /**支付   扣钱  扣钱记录*/
            User user= userMapper.selectById(order.getUserid());
            if(user.getPrice()>=order.getAllprice()){
                user.setPrice(user.getPrice()-order.getAllprice());
                userMapper.updateById(user);
                is_success=true;
                Earn earn=new Earn("支付佣金",order.getAllprice(),order.getUserid(),"1",DateUtil.now());
                earnMapper.insert(earn);
                if(!StringUtils.isEmpty(maincarid)){
                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(maincarid,order.getMaincarcolor());
                    carcolor.setInventory(carcolor.getInventory()-1);
                    carcolorMapper.updateById(carcolor);
                }
                if(!StringUtils.isEmpty(deputycarid)){
                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(deputycarid,order.getDeputycarcolor());
                    carcolor.setInventory(carcolor.getInventory()-order.getDeputycarnum());
                    carcolorMapper.updateById(carcolor);
                }

            }
        }
        return is_success;
    }

    @Override
    public Map<String,Object> reservationPay(Orders order,List<Map<String,Object>> carList,boolean is_pay_success,boolean is_pay,boolean is_chat) {
        String is_success="";
        Map<String,Object> result=new HashMap<>();
        if(order!=null&&carList!=null){
            /*order.setOrderid(Utils.getUUID());*/

            /*String maincarid=order.getMaincarid();
            String deputycarid=order.getDeputycarid();*/

                Orders orders=ordersMapper.selectById(order.getOrderid());
                if(orders==null){
                    order.setNotice("2");
                    order.setOrdernumber(Utils.getRandomTwelve());
                    order.setIscomment("0");
                    for(Map<String,Object> map:carList){
                        String carid=map.get("carid")==null?null:map.get("carid").toString();
                        String color=map.get("color")==null?null:map.get("color").toString();
                        Integer number=map.get("number")==null?null:(Integer)map.get("number");
                        /**0主车1副车*/
                        String type=map.get("type")==null?null:map.get("type").toString();
                        Officialcar offcar=officialcarMapper.selectById(carid);
                        if("0".equals(type)){
                            Orderinfo orderinfo=new Orderinfo();
                            orderinfo.setOrderid(order.getOrderid());
                            orderinfo.setCarid(carid);
                            orderinfo.setStatus("1");
                            orderinfo.setCarcolor(color);
                            orderinfo.setPrice(offcar.getCarprice());
                            orderinfo.setAddtime(DateUtil.now());
                            orderinfo.setUpdatetime(DateUtil.now());
                            orderinfo.setCartype("main");
                            if(is_pay_success){
                                orderinfo.setOrderpaystatus("1");
                                orderinfo.setType("1");
                                Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(carid,color);
                                carcolor.setInventory(carcolor.getInventory()-number);
                                carcolorMapper.updateById(carcolor);
                            }else{
                                orderinfo.setOrderpaystatus("0");
                                orderinfo.setType("0");
                            }
                            orderinfo.setSettlement("0");
                            orderInfoMapper.insert(orderinfo);

                        }else{
                            for (int i=0;i<number;i++){
                                Orderinfo orderinfo=new Orderinfo();
                                orderinfo.setOrderid(order.getOrderid());
                                orderinfo.setCarid(carid);
                                orderinfo.setStatus("1");
                                orderinfo.setCarcolor(color);
                                orderinfo.setPrice(offcar.getCarprice());
                                orderinfo.setAddtime(DateUtil.now());
                                orderinfo.setUpdatetime(DateUtil.now());
                                orderinfo.setCartype("deputy");
                                if(is_pay_success){
                                    orderinfo.setOrderpaystatus("1");
                                    orderinfo.setType("1");
                                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(carid,color);
                                    carcolor.setInventory(carcolor.getInventory()-1);
                                    carcolorMapper.updateById(carcolor);
                                }else{
                                    orderinfo.setOrderpaystatus("0");
                                    orderinfo.setType("0");
                                }
                                orderinfo.setSettlement("0");
                                orderInfoMapper.insert(orderinfo);
                            }

                        }

                    }
                    if(is_pay_success){
                        order.setOrderstatus("1");
                    }else{
                        order.setOrderstatus("0");
                    }
                    ordersMapper.insert(order);


                }else if(is_pay_success){
                    orders.setOrderstatus("1");
                    ordersMapper.updateById(orders);
                    List<Orderinfo> orderinfoList=orderInfoMapper.selectInfoByOrderid(orders.getOrderid());
                    orderinfoList.parallelStream().forEach((orderinfo)->{
                        orderinfo.setOrderpaystatus("1");
                        orderinfo.setType("1");
                        orderinfo.setUpdatetime(DateUtil.now());
                        orderInfoMapper.updateById(orderinfo);
                    });
                }
                Map<String,Object> map=new HashMap<>();
                if(orders!=null){
                    map.put("orderid",orders.getOrderid()+"_3");
                    map.put("price",orders.getAllprice());
                }else{
                    map.put("orderid",order.getOrderid()+"_3");
                    map.put("price",order.getAllprice());
                }
                map.put("subject","商品");
                if(is_pay){
                    is_success=Pay.AppPay(map);
                    result.put("order",is_success);
                }else if(is_pay_success){
                    is_success="支付成功";
                    result.put("order",is_success);
                }else if(is_chat){
                    SortedMap<Object, Object> sortedmap=WxPay.WxOrder(map);
                    result.put("order",sortedmap);
                }else if(!is_pay_success){
                    is_success="余额不足";
                    result.put("order",is_success);
                }
        }
        return result;
    }

    @Override
    public void changeStatus(String out_trade_no) {
        if(!StringUtils.isEmpty(out_trade_no)){

            Orders order=ordersMapper.selectById(out_trade_no);
            if(order!=null){
                if("0".equals(order.getOrderstatus())){
                    order.setOrderstatus("1");
                    ordersMapper.updateById(order);
                    List<Orderinfo> orderinfoList=orderInfoMapper.selectInfoByOrderid(order.getOrderid());
                    orderinfoList.parallelStream().forEach((orderinfo)->{
                        orderinfo.setOrderpaystatus("1");
                        orderinfo.setType("1");
                        orderinfo.setUpdatetime(DateUtil.now());
                        orderInfoMapper.updateById(orderinfo);
                    });
                }
            }else{
                Orderinfo orderinfo=orderInfoMapper.selectById(out_trade_no);
                if(orderinfo!=null){
                    if("0".equals(orderinfo.getType())){
                        orderinfo.setOrderpaystatus("1");
                        /**车主进入待支付保证金状态*/
                        orderinfo.setType("1");
                        orderinfo.setUpdatetime(DateUtil.now());
                        orderinfo.setMargin(screen(orderinfo.getPrice()));
                        orderInfoMapper.updateById(orderinfo);
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition("用户已付款");
                        Car car=carMapper.selectById(orderinfo.getCarid());
                        notice.setContent("您的"+orderinfo.getCarcolor()+car.getType()+car.getCarmodel()+"已被新人预定，请在30分钟内支付保证金，否则订单取消！");

                        User infouser=userMapper.selectById(orderinfo.getUserid());
                        if(!StringUtils.isEmpty(infouser.getCid())){
                            Map<String,Object> map=new HashMap<>();
                            map.put("cid",infouser.getCid());
                            map.put("title",notice.getAddition());
                            map.put("text",notice.getContent());
                            map.put("version", infouser.getVersion());
                            PushtoSingle.push(map);
                        }

                        notice.setIsread("0");
                        notice.setUserid(orderinfo.getUserid());
                        noticeMapper.insert(notice);
                    }else if("1".equals(orderinfo.getType())&&"0".equals(orderinfo.getCarmainpaystatus())){
                        orderinfo.setCarmainpaystatus("1");
                        orderinfo.setRealprice(orderinfo.getPrice()+orderinfo.getMargin());
                        orderinfo.setCause("支付保证金减少"+orderinfo.getMargin()+"元;");
                        orderinfo.setIs_sign("0");
                        orderinfo.setSettlement("0");
                        orderInfoMapper.updateById(orderinfo);
                        detectionOrder(orderinfo.getOrderinfoid());
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition("车主已付款");
                        Car car=carMapper.selectById(orderinfo.getCarid());
                        notice.setContent("您选中的"+orderinfo.getCarcolor()+car.getType()+car.getCarmodel()+"已被车主支付保证金,请等待其他车主付款！");
                        User ordereuser=userMapper.selectById(order.getUserid());
                        if(!StringUtils.isEmpty(ordereuser.getCid())){
                            Map<String,Object> map=new HashMap<>();
                            map.put("cid",ordereuser.getCid());
                            map.put("title",notice.getAddition());
                            map.put("text",notice.getContent());
                            map.put("version", ordereuser.getVersion());
                            PushtoSingle.push(map);
                        }

                        notice.setIsread("0");
                        notice.setUserid(order.getUserid());
                        noticeMapper.insert(notice);
                    }
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> selectTakeMyOrderInfo(Map<String, Object> map) {
        List<Map<String, Object>> listMap=new ArrayList<>();
        List<Map<String, Object>> resultMap=new ArrayList<>();
        if(map!=null){
            listMap=ordersMapper.selectTakeMyOrderInfo(map);
            for(Map<String,Object> result: listMap){
                String orderstauts=result.get("orderstatus")==null?null:result.get("orderstatus").toString();
                String type=map.get("type")==null?null:map.get("type").toString();
                if("0".equals(orderstauts)&&"0".equals(type)){
                    resultMap.add(result);
                }else if("0".equals(orderstauts)&&"1".equals(type)){
                    resultMap.add(result);
                }else if("1".equals(orderstauts)&&"2".equals(type)){
                    resultMap.add(result);
                }else if("2".equals(orderstauts)&&"3".equals(type)){
                    resultMap.add(result);
                }

            }

        }
        return resultMap;
    }

    @Override
    public boolean myReleasePayOrderInfo(String orderinfoid,String userid) {
        boolean is_success=false;
        if(!StringUtils.isEmpty(orderinfoid)){
            Orderinfo orderinfo=orderInfoMapper.selectById(orderinfoid);
            /**支付金额*/
            /**查询用户 金额  减去车辆金额 修改数据库*/
            User user= userMapper.selectById(userid);
            Double price=orderinfo.getPrice();
            if(price!=null){
                if(user.getPrice()>=price){
                    user.setPrice(user.getPrice()-price);
                    userMapper.updateById(user);
                    Earn earn=new Earn("租车款",price,userid,"1",DateUtil.now());
                    earnMapper.insert(earn);
                    is_success=true;
                    /**修改订单状态*/
                    /**订单方支付*/
                    orderinfo.setOrderpaystatus("1");
                    /**车主进入待支付保证金状态*/
                    orderinfo.setType("1");
                    orderinfo.setUpdatetime(DateUtil.now());
                    orderinfo.setMargin(screen(orderinfo.getPrice()));
                    orderInfoMapper.updateById(orderinfo);
                    Notice notice=new Notice();
                    notice.setAddtime(DateUtil.now());
                    notice.setAddition("用户已付款");
                    Car car=carMapper.selectById(orderinfo.getCarid());
                    notice.setContent("您的"+orderinfo.getCarcolor()+car.getType()+car.getCarmodel()+"已被新人预定，请在30分钟内支付保证金，否则订单取消！");

                    User infouser=userMapper.selectById(orderinfo.getUserid());
                    if(!StringUtils.isEmpty(infouser.getCid())){
                        Map<String,Object> map=new HashMap<>();
                        map.put("cid",infouser.getCid());
                        map.put("title",notice.getAddition());
                        map.put("text",notice.getContent());
                        map.put("version", infouser.getVersion());
                        PushtoSingle.push(map);
                    }

                    notice.setIsread("0");
                    notice.setUserid(orderinfo.getUserid());
                    noticeMapper.insert(notice);
                }
            }
        }
        return is_success;
    }

    @Override
    public boolean takeMyOrderInfoMargin(String orderinfoid, String userid) {
        boolean is_success=false;
        if(!StringUtils.isEmpty(orderinfoid)&&!StringUtils.isEmpty(userid)){
            /**查询用户金额  使用用户金额减去保证金  对金钱记录中加上一笔*/
            Orderinfo orderinfo=orderInfoMapper.selectById(orderinfoid);
            Orders order=ordersMapper.selectById(orderinfo.getOrderid());
                /**用户金额减去保证金*/
                User user=userMapper.selectById(userid);
                Double price=user.getPrice();
                if(user.getPrice()>=orderinfo.getMargin()){
                    is_success=true;
                    user.setPrice(user.getPrice()-orderinfo.getMargin());
                    /**更新用户表*/
                    Earn earn=new Earn("支付保证金",Double.valueOf(orderinfo.getMargin()),userid,"1",DateUtil.now());
                    earnMapper.insert(earn);
                    /**佣金记录*/
                    userMapper.updateById(user);
                    orderinfo.setCarmainpaystatus("1");
                    orderinfo.setRealprice(orderinfo.getPrice()+orderinfo.getMargin());
                    orderinfo.setCause("支付保证金减少"+orderinfo.getMargin()+"元;");
                    orderinfo.setIs_sign("0");
                    orderinfo.setSettlement("0");
                    orderInfoMapper.updateById(orderinfo);
                    Notice notice=new Notice();
                    notice.setAddtime(DateUtil.now());
                    notice.setAddition("车主已付款");
                    Car car=carMapper.selectById(orderinfo.getCarid());
                    notice.setContent("您选中的"+orderinfo.getCarcolor()+car.getType()+car.getCarmodel()+"已被车主支付保证金,请等待其他车主付款！");
                    User ordereuser=userMapper.selectById(order.getUserid());
                    if(!StringUtils.isEmpty(ordereuser.getCid())){
                        Map<String,Object> map=new HashMap<>();
                        map.put("cid",ordereuser.getCid());
                        map.put("title",notice.getAddition());
                        map.put("text",notice.getContent());
                        map.put("version", ordereuser.getVersion());
                        PushtoSingle.push(map);
                    }

                    notice.setIsread("0");
                    notice.setUserid(order.getUserid());
                    noticeMapper.insert(notice);
                }
                /**如果订单已经接完了  退出*/
                /**查询订单的主车和副车都没有了*/

        }
        return is_success;
    }

    @Override
    public Map<String,Object> pay(String orderinfoid,boolean is_chat) {
        Map<String,Object> result=new HashMap<>();
        if(!StringUtils.isEmpty(orderinfoid)){
           Orderinfo orderinfo= orderInfoMapper.selectById(orderinfoid);
            Map<String,Object> map=new HashMap<>();

            if("0".equals(orderinfo.getType())){
                map.put("orderid",orderinfo.getOrderinfoid()+"_1");
                map.put("subject","商品");
                map.put("price",orderinfo.getPrice());
            }else{
                map.put("orderid",orderinfo.getOrderinfoid()+"_2");
                map.put("subject","商品");
                map.put("price",orderinfo.getMargin());
            }
            if(is_chat){
                //微信
                result.put("order",WxPay.WxOrder(map));
            }else{
                //支付宝
                result.put("order",Pay.AppPay(map));
            }


        }
        return result;
    }

    @Override
    public boolean selectIsSign(String userid,String orderid) {
        boolean is_success=false;
        if(!StringUtils.isEmpty(userid)){
            List<Orderinfo> orderinfoList=orderInfoMapper.selectOrderinfoByUseridAndOrderid(userid,orderid);
            String isSign=orderinfoList.get(0).getIs_sign();
            if("1".equals(isSign)){
                is_success=true;
            }
        }
        return is_success;
    }

    @Override
    public void detectionOrder(String orderinfoid) {
        Orderinfo orderinfo=orderInfoMapper.selectById(orderinfoid);
        if(!StringUtils.isEmpty(orderinfoid)){
            Map<String,Object> map=ordersMapper.selectMainOrderInfoByNum(1,orderinfo.getOrderid());
            String main=map.get("mainRemain").toString();
            if("0".equals(main)&&!StringUtils.isEmpty(main)){
                map=ordersMapper.selectDeputyOrderInfoByNum(orderinfo.getOrderid());
                String deputy=null;
                if(map.containsKey("deputyRemain")){
                    deputy=map.get("deputyRemain").toString();
                }
                if(deputy==null||("0".equals(deputy)&&!StringUtils.isEmpty(deputy))){
                    /**将大订单状态更改为进行中*/
                    Orders order=ordersMapper.selectById(orderinfo.getOrderid());
                    order.setOrderstatus("1");
                    ordersMapper.updateById(order);
                    List<Orderinfo> orderinfoListCar=orderInfoMapper.selectOrderInfoByOrderIdAndStatus(orderinfo.getOrderid(),"0");
                    for (Orderinfo info:orderinfoListCar) {
                        if("1".equals(info.getCarmainpaystatus())&&"1".equals(info.getOrderpaystatus())){
                            info.setType("2");
                            orderInfoMapper.updateById(info);
                            Notice notice=new Notice();
                            notice.setAddtime(DateUtil.now());
                            notice.setAddition("订单进行中");

                            notice.setContent("您的订单到达进行中状态，请耐心等待新人的来电！");

                            User user=userMapper.selectById(info.getUserid());
                            if(!StringUtils.isEmpty(user.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",user.getCid());
                                result.put("title",notice.getAddition());
                                result.put("text",notice.getContent());
                                map.put("version", user.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice.setIsread("0");
                            notice.setUserid(info.getUserid());
                            noticeMapper.insert(notice);
                        }else{
                            Car car=carMapper.selectById(info.getCarid());
                            car.setStarttime(DateUtil.now());
                            car.setEndtime(DateUtil.now());
                            carMapper.updateById(car);
                        }
                    }
                    Notice notice=new Notice();
                    notice.setAddtime(DateUtil.now());
                    notice.setAddition("订单进行中");

                    notice.setContent("您的订单到达进行中状态，请耐心等待,婚宴前48小时可通过平台查看车主联系方式！");

                    User user=userMapper.selectById(order.getUserid());
                    if(!StringUtils.isEmpty(user.getCid())){
                        Map<String,Object> result=new HashMap<>();
                        result.put("cid",user.getCid());
                        result.put("title",notice.getAddition());
                        result.put("text",notice.getContent());
                        map.put("version", user.getVersion());
                        PushtoSingle.push(result);
                    }

                    notice.setIsread("0");
                    notice.setUserid(order.getUserid());
                    noticeMapper.insert(notice);
                    orderInfoMapper.deleteByOrderIdAndType(order.getOrderid());
                }
            }
        }
    }

    @Override
    public void cancelAllOrder(String orderid,String userid) {

        if(!StringUtils.isEmpty(orderid)){
            /**查询接单车主的订单*/
            Orders order=ordersMapper.selectById(orderid);
            User user=userMapper.selectById(order.getUserid());
            List<Orderinfo> orderinfoTakeList=orderInfoMapper.selectOrderInfoByOrderIdAndStatus(orderid,"0");
            if(orderinfoTakeList.size()>0){
                    for(Orderinfo orderinfo:orderinfoTakeList){
                        String orderinfoUserid=orderinfo.getUserid();
                        User infoUser=userMapper.selectById(orderinfo.getUserid());
                        if("1".equals(orderinfo.getOrderpaystatus())&&"0".equals(orderinfo.getCarmainpaystatus())){
                            /**退还佣金就可以了*/
                            user.setPrice(user.getPrice()+orderinfo.getPrice());
                            userMapper.updateById(user);
                            Earn earn=new Earn("退还佣金",orderinfo.getPrice(),user.getUserid(),"0",DateUtil.now());
                            earnMapper.insert(earn);
                            Notice notice=new Notice();
                            notice.setAddtime(DateUtil.now());
                            notice.setAddition(ConstantUtils.ORDERCANCEL);
                            notice.setContent("您的订单已被新人取消，因未支付出车保证金，此订单为无责取消！");


                            if(!StringUtils.isEmpty(infoUser.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",infoUser.getCid());
                                result.put("title",notice.getAddition());
                                result.put("text",notice.getContent());
                                result.put("version", infoUser.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice.setIsread("0");
                            notice.setUserid(orderinfo.getUserid());
                            noticeMapper.insert(notice);

                            Notice notice1=new Notice();
                            notice1.setAddtime(DateUtil.now());
                            notice1.setAddition(ConstantUtils.ORDERCANCEL);
                            notice1.setContent("您的订单已取消，用车款已退回到您账户！");


                            if(!StringUtils.isEmpty(user.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",user.getCid());
                                result.put("title",notice1.getAddition());
                                result.put("text",notice1.getContent());
                                result.put("version", user.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice1.setIsread("0");
                            notice1.setUserid(user.getUserid());
                            noticeMapper.insert(notice1);


                        }else if("1".equals(orderinfo.getOrderpaystatus())&&"1".equals(orderinfo.getCarmainpaystatus())){
                            /**退还一半佣金 另一半佣金和保证金退还给车主   待解决*/
                            User orderinfouser=userMapper.selectById(orderinfoUserid);
                            orderinfouser.setPrice(orderinfouser.getPrice()+(orderinfo.getPrice()/2)+orderinfo.getMargin());
                            userMapper.updateById(orderinfouser);
                            Earn earn=new Earn("新人违约金",(orderinfo.getPrice()/2)+orderinfo.getMargin(),orderinfoUserid,"0",DateUtil.now());
                            earnMapper.insert(earn);
                            Notice notice=new Notice();
                            notice.setAddtime(DateUtil.now());
                            notice.setAddition(ConstantUtils.ORDERCANCEL);
                            notice.setContent("您的订单已被新人取消，新人将赔付您一定赔偿金！");

                            if(!StringUtils.isEmpty(infoUser.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",infoUser.getCid());
                                result.put("title",notice.getAddition());
                                result.put("text",notice.getContent());
                                result.put("version", infoUser.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice.setIsread("0");
                            notice.setUserid(orderinfo.getUserid());
                            noticeMapper.insert(notice);
                            Notice notice1=new Notice();
                            notice1.setAddtime(DateUtil.now());
                            notice1.setAddition("您已违约");
                            notice1.setContent("您的订单已违约，平台将扣除您的用车全款作为违约金！");

                            if(!StringUtils.isEmpty(user.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",user.getCid());
                                result.put("title",notice1.getAddition());
                                result.put("text",notice1.getContent());
                                result.put("version", user.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice1.setIsread("0");
                            notice1.setUserid(user.getUserid());
                            noticeMapper.insert(notice1);
                        }else if("0".equals(orderinfo.getOrderpaystatus())&&"0".equals(orderinfo.getCarmainpaystatus())){
                            Notice notice=new Notice();
                            notice.setAddtime(DateUtil.now());
                            notice.setAddition(ConstantUtils.ORDERCANCEL);
                            notice.setContent("您的订单已被新人取消，再试试其他订单吧！");

                            if(!StringUtils.isEmpty(infoUser.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",infoUser.getCid());
                                result.put("title",notice.getAddition());
                                result.put("text",notice.getContent());
                                result.put("version", infoUser.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice.setIsread("0");
                            notice.setUserid(orderinfo.getUserid());
                            noticeMapper.insert(notice);

                            Notice notice1=new Notice();
                            notice1.setAddtime(DateUtil.now());
                            notice1.setAddition("您的订单已取消");
                            notice1.setContent("您的订单已取消，请重新提交订单！");

                            if(!StringUtils.isEmpty(user.getCid())){
                                Map<String,Object> result=new HashMap<>();
                                result.put("cid",user.getCid());
                                result.put("title",notice1.getAddition());
                                result.put("text",notice1.getContent());
                                result.put("version", user.getVersion());
                                PushtoSingle.push(result);
                            }

                            notice1.setIsread("0");
                            notice1.setUserid(user.getUserid());
                            noticeMapper.insert(notice1);
                        }
                        /**车时间解开*/
                        Car carMargin=carMapper.selectById(orderinfo.getCarid());
                        carMargin.setStarttime(DateUtil.now());
                        carMargin.setEndtime(DateUtil.now());
                        carMapper.updateById(carMargin);
                        /**删除子订单*/
                        orderInfoMapper.deleteById(orderinfo.getOrderinfoid());
                    }


            }

            /**查询预定的订单*/
            List<Orderinfo> orderinfoReserveList=orderInfoMapper.selectOrderInfoByOrderIdAndStatus(orderid,"1");
            /*if(orderinfoReserveList.size()>0){
                orderinfoReserveList.parallelStream().forEach((orderinfo)->{
                    Integer price=orderinfo.getPrice();
                    *//**查询用户金额  根据用户id*//*
                    User user=userMapper.selectById(userid);

                    user.setPrice(user.getPrice()+price/2);
                    *//**加上一条收益记录*//*
                    Earn earn =new Earn("订单方违约",price/2,user.getUserid(),"0",DateUtil.now());
                    earnMapper.insert(earn);
                    *//**更新用户表*//*
                    userMapper.updateById(user);
                });
            }*/

            if(orderinfoReserveList.size()<1){
                ordersMapper.deleteById(orderid);
            }

        }

    }

    @Override
    public void cancelAllOrderinfo(String orderid, String userid) {
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(userid)){
            Map<String,Object> map=new HashMap<>();
            map.put("orderid",orderid);
            map.put("userid",userid);
            Orders order=ordersMapper.selectById(orderid);
            /**订单方用户*/
            User user=userMapper.selectById(order.getUserid());

            List<Orderinfo> orderinfoList=orderInfoMapper.selectOrderinfoByUseridAndOrderidAndType(map);

                /**归还订单方车钱*/
                for(Orderinfo orderinfo:orderinfoList){
                    User infoUser=userMapper.selectById(orderinfo.getUserid());
                    if("1".equals(orderinfo.getOrderpaystatus())&&"0".equals(orderinfo.getCarmainpaystatus())){
                        /**退还订单方佣金*/
                        user.setPrice(user.getPrice()+orderinfo.getPrice());
                        userMapper.updateById(user);
                        Earn earn=new Earn("退回佣金",orderinfo.getPrice(),user.getUserid(),"0",DateUtil.now());
                        earnMapper.insert(earn);
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition(ConstantUtils.ORDERCANCEL);
                        notice.setContent("您的订单已被车主取消，请尽快预定其他车辆！");
                        notice.setUserid(user.getUserid());
                        notice.setIsread("0");
                        noticeMapper.insert(notice);
                        if(!StringUtils.isEmpty(user.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",user.getCid());
                            result.put("title",notice.getAddition());
                            result.put("text",notice.getContent());
                            result.put("version", user.getVersion());
                            PushtoSingle.push(result);
                        }

                        Notice notice1=new Notice();
                        notice1.setAddtime(DateUtil.now());
                        notice1.setAddition(ConstantUtils.ORDERCANCEL);
                        notice1.setContent("您已取消订单，本次订单为无责取消！");
                        notice1.setUserid(infoUser.getUserid());
                        notice1.setIsread("0");
                        noticeMapper.insert(notice1);
                        if(!StringUtils.isEmpty(infoUser.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",infoUser.getCid());
                            result.put("title",notice1.getAddition());
                            result.put("text",notice1.getContent());
                            result.put("version", infoUser.getVersion());
                            PushtoSingle.push(result);
                        }
                    }else if("1".equals(orderinfo.getOrderpaystatus())&&"1".equals(orderinfo.getCarmainpaystatus())){
                        user.setPrice(user.getPrice()+orderinfo.getMargin()+orderinfo.getPrice());
                        userMapper.updateById(user);
                        Earn earn=new Earn(ConstantUtils.DRIVERBREACH,orderinfo.getMargin()+orderinfo.getPrice(),user.getUserid(),"0",DateUtil.now());
                        earnMapper.insert(earn);

                        Car car=carMapper.selectById(orderinfo.getCarid());
                        car.setStarttime(DateUtil.now());
                        car.setEndtime(DateUtil.now());
                        carMapper.updateById(car);
                        Notice notice=new Notice();
                        notice.setAddtime(DateUtil.now());
                        notice.setAddition(ConstantUtils.ORDERCANCEL);
                        notice.setContent("您的订单已被车主取消，车主将赔付您一定的赔偿金！");

                        /*User infoUser=userMapper.selectById(orderinfo.getUserid());*/
                        if(!StringUtils.isEmpty(user.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",user.getCid());
                            result.put("title",notice.getAddition());
                            result.put("text",notice.getContent());
                            result.put("version", user.getVersion());
                            PushtoSingle.push(result);
                        }

                        notice.setIsread("0");
                        notice.setUserid(user.getUserid());
                        noticeMapper.insert(notice);

                        Notice notice1=new Notice();
                        notice1.setAddtime(DateUtil.now());
                        notice1.setAddition("您已违约");
                        notice1.setContent("您的订单已违约，已扣除您的出车保证金作为违约金！");

                        /*User infoUser=userMapper.selectById(orderinfo.getUserid());*/
                        if(!StringUtils.isEmpty(infoUser.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",infoUser.getCid());
                            result.put("title",notice1.getAddition());
                            result.put("text",notice1.getContent());
                            result.put("version", infoUser.getVersion());
                            PushtoSingle.push(result);
                        }

                        notice1.setIsread("0");
                        notice1.setUserid(infoUser.getUserid());
                        noticeMapper.insert(notice1);

                    }
                    /**车时间解开*/
                    Car carMargin=carMapper.selectById(orderinfo.getCarid());
                    if(carMargin!=null){
                        carMargin.setStarttime(DateUtil.now());
                        carMargin.setEndtime(DateUtil.now());
                        carMapper.updateById(carMargin);
                    }


                    orderInfoMapper.deleteById(orderinfo.getOrderinfoid());

                }
                if("1".equals(order.getOrderstatus())){
                    Integer count = orderInfoMapper.selectUnfinished(order.getOrderid());
                    if(count<1){
                        ordersMapper.deleteById(orderid);
                    }
                }



        }
    }

    @Override
    public void cancelOrderinfo(String orderinfoid) {
        if(!StringUtils.isEmpty(orderinfoid)){
           Map<String,Object> map= orderInfoMapper.selectOrderByOrderinfoId(orderinfoid);
           if(map!=null){
               Integer price=(Integer)map.get("price");
               String userid=map.get("userid").toString();
               String type=map.get("type").toString();
               if(!StringUtils.isEmpty(type)&&"1".equals(type)){
                 User user= userMapper.selectById(userid);
                 user.setPrice(user.getPrice()+price);
                 userMapper.updateById(user);
               }
               orderInfoMapper.deleteById(orderinfoid);
           }

        }
    }

    @Override
    public void cancelOrderinfoReserve(String orderid, String type,List<Map<String,Object>> carList,String userid) {
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(type)&&!StringUtils.isEmpty(userid)&&carList!=null){
            List<Map<String,Object>> map=orderInfoMapper.selectReserveOrderinfoByOrderidAndType(orderid,type);
            if("1".equals(type)||"2".equals(type)){
                User user=userMapper.selectById(userid);
                Double price=user.getPrice();
                Orders order=ordersMapper.selectById(orderid);
               /* map.parallelStream().forEach((orderinfoMap)->{
                    user.setPrice(user.getPrice()+Double.valueOf(orderinfoMap.get("price").toString())/2);
                });*/
                user.setPrice(user.getPrice()+order.getAllprice()/2);
                /**添加记录*/
                price=user.getPrice()-price;
                Earn earn=new Earn("违约金",price,userid,"0",DateUtil.now());
                earnMapper.insert(earn);
                userMapper.updateById(user);


                carList.parallelStream().forEach((car)->{
                   String carid=car.get("carid")==null?null:car.get("carid").toString();
                   String color=car.get("color")==null?null:car.get("color").toString();
                   Integer number=car.get("number")==null?null:(Integer)car.get("number");
                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(carid,color);
                    carcolor.setInventory(carcolor.getInventory()+number);
                    carcolorMapper.updateById(carcolor);
                });

            }
            /**查询这个订单是否还有其他不属于预定的订单   如果没有删除大订单*/
           /* Integer count=orderInfoMapper.selectOrderInfoByOrderId(orderid);
            if(count<1){
                ordersMapper.deleteById(orderid);
            }*/
           Orders order=ordersMapper.selectById(orderid);
           if(!StringUtils.isEmpty(order.getMaincartype())||!StringUtils.isEmpty(order.getDeputycartype())){
               ordersMapper.deleteById(orderid);
           }
            map.parallelStream().forEach((orderinfoMap)->{
                String orderinfoid=orderinfoMap.get("orderinfoid").toString();
                orderInfoMapper.deleteById(orderinfoid);
            });
        }
    }

    @Override
    public void releaseEval(Commentuser commentuser, String userid) {
        if(commentuser!=null&&!StringUtils.isEmpty(userid)){
            if(StringUtils.isEmpty(commentuser.getCommuserid())){
                Orders order=ordersMapper.selectById(commentuser.getOrderid());
                if(order!=null){
                    order.setIscomment("1");
                    ordersMapper.updateById(order);
                }
            }
            commentuser.setCommentid(Utils.getUUID());
            commentuser.setCommuserid(userid);
            commentuser.setAddtime(DateUtil.now());
            commentuserMapper.insert(commentuser);
        }
    }

    @Override
    public List<Map<String, Object>> myEval(String userid) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){
            listMap=commentuserMapper.selectMyEval(userid);
        }
        return listMap;
    }

    @Override
    public void myFeedback(String userid, Fendback fendback) {
        if(!StringUtils.isEmpty(userid)&&fendback!=null){
            fendback.setFeedbackid(Utils.getUUID());
            fendback.setUserid(userid);
            fendback.setAddtime(DateUtil.now());
            fendbackMapper.insert(fendback);
        }
    }

    @Override
    public List<Earn> selectEarnByAddtime(String userid, String addtime) {
           List<Earn> earnList=new LinkedList<>();
            if(!StringUtils.isEmpty(userid)){
                earnList=earnMapper.selectEarnByAddtime(userid,addtime);
            }
            return earnList;
    }

    @Override
    public void addAccount(String userid, Account account) {
        if(!StringUtils.isEmpty(userid)&&account!=null){
            account.setAccountid(Utils.getUUID());
            account.setUserid(userid);
            account.setAddtime(DateUtil.now());
            accountMapper.insert(account);
        }
    }

    @Override
    public void updateAccount(Account account) {
       if(account!=null){
            account.setUpdatetime(DateUtil.now());
            accountMapper.updateById(account);
        }
    }

    @Override
    public Account selectAccount(String userid, String ditch) {
        Account account =null;
        if(!StringUtils.isEmpty(userid)&&!StringUtils.isEmpty(ditch)){
            account=accountMapper.selectAccountByUseridAndDitch(userid,ditch);
        }
        return account;
    }

    @Override
    public Map<String,Object> yesterdayEarnAndprice(String userid,String day) {
        Map<String,Object> earnAndPrice=new HashMap<>();
        if(!StringUtils.isEmpty(userid)&&!StringUtils.isEmpty(day)){
            String yesterdayprice=earnMapper.yesterdayEarnAndprice(userid,day);
            if(!StringUtils.isEmpty(yesterdayprice)){
                earnAndPrice.put("yesterdayEarn",yesterdayprice);
            }else{
                earnAndPrice.put("yesterdayEarn","0");
            }
            earnAndPrice.put("price",String.valueOf(userMapper.selectById(userid).getPrice()));

        }
        return earnAndPrice;
    }

    @Override
    public User selectUser(String userid) {
        User user=null;
        if(!StringUtils.isEmpty(userid)){
            user=userMapper.selectUser(userid);
            Map<String,Object> realnameMap=realnameMapper.selectRealNameByUserId(userid);
            if(realnameMap!=null&&!realnameMap.isEmpty()){
                if(realnameMap.containsKey("realstatus")){
                    user.setRealname(realnameMap.get("realstatus")==null?null:realnameMap.get("realstatus").toString());
                }
            }
            Map<String,Object> licenseMap=licenseMapper.selectLicenseByUserId(userid);
            if(licenseMap!=null&&!licenseMap.isEmpty()){
                if(licenseMap.containsKey("licensestatus")){
                    user.setLicense(licenseMap.get("licensestatus")==null?null:licenseMap.get("licensestatus").toString());
                }
            }
        }
        return user;
    }

    @Override
    public Result carReach(String userid, String orderid) {
        Result result=new Result();
        if(!StringUtils.isEmpty(orderid)) {
            Orders order = ordersMapper.selectById(orderid);
            if(order==null){
                return Result.fail("订单已失效！");
            }
            String marrytime = order.getMarrytime();
            String format = DateUtil.format(DateUtil.parse(marrytime), "yyyy-MM-dd");
            String day = DateUtil.today();
            if (day.equals(format)){
                Integer todaytime = Integer.valueOf(DateUtil.format(new Date(), "HH"));
                Integer marryHour = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "HH"));
                if (!(todaytime >= (marryHour - 2))) {
                    return Result.fail("未到时间");
                } else if (!StringUtils.isEmpty(userid)) {
                    User user = userMapper.selectById(order.getUserid());
                    /**查询我的所有车辆订单*/
                    List<Orderinfo> orderinfoList = orderInfoMapper.selectOrderinfoByUseridAndOrderid(userid, orderid);
                    if(orderinfoList.size()==0){
                        return Result.fail("订单已失效！");
                    }
                    Integer marry = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "HH"));
                    Integer today = Integer.valueOf(DateUtil.format(new Date(), "HH"));
                    Integer marry_minutes = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "mm"));
                    Integer today_minutes = Integer.valueOf(DateUtil.format(new Date(), "mm"));
                    Double breach = 0.0;
                    boolean isRefund = false;
                    boolean isWarning = false;
                    long time = DateUtil.between(new Date(), DateUtil.parse(order.getMarrytime()), DateUnit.MINUTE);
                    String success=null;
                    Map<String,Object> map=new HashMap<>();
                    Map<String,Object> map1=new HashMap<>();
                    User infoUser=userMapper.selectById(orderinfoList.get(0).getUserid());
                    User orderUser=userMapper.selectById(order.getUserid());
                    if (today >= marry||(today==marry&&today_minutes>=marry_minutes)) {

                        if (time > 0 && time <= 15) {
                            /**发送警告*/
                            isWarning = true;
                            result.setMessage(ConstantUtils.LATE+time+"分钟");
                            result.setSuccess(false);
                            success="发出警告;";
                            map.put("title",ConstantUtils.LATE);
                            map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");



                            map1.put("title",ConstantUtils.DRIVERLATE);
                            map1.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                        } else if (time > 15 && time <= 30) {
                            /**扣除30%保证金交给订车用户*/
                            breach = 0.3;
                            isWarning = true;
                            success="发出警告;";
                            result.setMessage(ConstantUtils.LATE+time+"分钟");
                            result.setSuccess(false);
                            map.put("title",ConstantUtils.LATE);
                            map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");

                            map1.put("title",ConstantUtils.DRIVERLATE);
                            map1.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                        } else if (time > 30 && time <= 60) {
                            /**扣除50保证金  并把订车用户的钱归还给订车用户*/
                            breach = 0.5;
                            isWarning = true;
                            success="发出警告;";
                            result.setMessage(ConstantUtils.LATE+time+"分钟");
                            result.setSuccess(false);

                            map.put("title",ConstantUtils.LATE);
                            map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");

                            map1.put("title",ConstantUtils.DRIVERLATE);
                            map1.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                        } else if(time>60) {
                            /**扣除100%保证金   并把订车用户的钱归还给订车用户*/
                            breach = 1.0;
                            isRefund = true;
                            result.setMessage(ConstantUtils.LATE+time+"分钟，将视为缺席");
                            result.setSuccess(false);
                            /**将车主拉入黑名单   未写*/
                            map.put("title","您已缺席");
                            map.put("text","您缺席，将扣除您100%保证金和用车租金！");


                            map1.put("title","车主已缺席");
                            map1.put("text","车主已缺席，车主出车保证金已赔付到您账户！");
                        }
                    }else{
                        //正常到达
                        isWarning=true;
                        result.setSuccess(true);
                        success="您已正常到达;";
                        map.put("title","您已正常到达");
                        map.put("text","您已正常到达，请联系新人");


                        map1.put("title","车主已正常到达");
                        map1.put("text","车主已到达，车辆已到达指定位置，祝您用车愉快！");
                    }
                    if(!StringUtils.isEmpty(infoUser.getCid())){
                        map.put("cid",infoUser.getCid());
                        map.put("version", infoUser.getVersion());
                        PushtoSingle.push(map);
                        Notice notice=new Notice();
                        notice.setUserid(infoUser.getUserid());
                        notice.setAddtime(DateUtil.now());
                        notice.setIsread("0");
                        notice.setAddition(map.get("title").toString());
                        notice.setContent(map.get("text").toString());
                        noticeMapper.insert(notice);
                    }
                    if(!StringUtils.isEmpty(orderUser.getCid())){
                        map1.put("cid",orderUser.getCid());
                        map1.put("version", orderUser.getVersion());
                        PushtoSingle.push(map1);
                        Notice notice=new Notice();
                        notice.setUserid(orderUser.getUserid());
                        notice.setAddtime(DateUtil.now());
                        notice.setIsread("0");
                        notice.setAddition(map1.get("title").toString());
                        notice.setContent(map1.get("text").toString());
                        noticeMapper.insert(notice);
                    }


                    /**对我的订单进行相应处理*/
                    for (Orderinfo orderinfo : orderinfoList) {
                        orderinfo.setIs_sign("1");
                        if (!isWarning) {
                            Integer b = Double.valueOf(orderinfo.getMargin() * breach).intValue();
                            orderinfo.setCause(orderinfo.getCause() + "迟到" + time + "分钟,扣除保证金" + breach * 100 + "%,减少" + b + "元;");

                            if (isRefund) {
                                orderinfo.setRealprice(orderinfo.getRealprice() - b - orderinfo.getPrice());
                                orderinfo.setCause(orderinfo.getCause() + "扣除订车用户佣金,减少" + orderinfo.getPrice() + "元;");

                            }
                        } else {
                            orderinfo.setCause(orderinfo.getCause() + success);
                        }
                        orderInfoMapper.updateById(orderinfo);
                    }

                }
            } else {
                return Result.fail("未到结婚日期");
            }
        }
        return result;
    }



    @Override
    public void extendsTime() {
        CronUtil.schedule("*/1 * * * *", new Task() {
            @Override
            public void execute() {
                /*System.out.println("一分钟一次"+DateUtil.now());*/
                List<Orderinfo> orderinfoLate = orderInfoMapper.selectOrderinfoLate();
                orderinfoLate.parallelStream().forEach((orderinfo) -> {
                    Orders order = ordersMapper.selectById(orderinfo.getOrderid());
                    String marrytime = order.getMarrytime();
                    String format = DateUtil.format(DateUtil.parse(marrytime), "yyyy-MM-dd");
                    String day = DateUtil.today();
                    if (day.equals(format)){
                            Integer marry = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "HH"));
                            Integer today = Integer.valueOf(DateUtil.format(new Date(), "HH"));
                            Integer marry_minutes = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "mm"));
                            Integer today_minutes = Integer.valueOf(DateUtil.format(new Date(), "mm"));
                            long time = DateUtil.between(new Date(), DateUtil.parse(order.getMarrytime()), DateUnit.MINUTE);
                            if (today > marry||(today==marry&&today_minutes>=marry_minutes)) {
                                Map<String,Object> map=new HashMap<>();
                                Map<String,Object> result=new HashMap<>();
                                User infoUser=userMapper.selectById(orderinfo.getUserid());
                                User user=userMapper.selectById(order.getUserid());
                                Notice infoNotice=new Notice();
                                infoNotice.setIsread("0");
                                Notice userNotice=new Notice();
                                userNotice.setIsread("0");
                                if (time > 0 && time <= 15&&"1".equals(orderinfo.getTime())) {
                                    /*发送警告*/

                                    map.put("title",ConstantUtils.LATE);
                                    map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点");
                                    infoNotice.setAddition(ConstantUtils.LATE);
                                    infoNotice.setContent(ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");
                                    infoNotice.setAddtime(DateUtil.now());
                                    infoNotice.setUserid(infoUser.getUserid());
                                    noticeMapper.insert(infoNotice);


                                    result.put("title",ConstantUtils.DRIVERLATE);
                                    result.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系");
                                    userNotice.setAddition(ConstantUtils.DRIVERLATE);
                                    userNotice.setContent(ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                                    userNotice.setAddtime(DateUtil.now());
                                    userNotice.setUserid(user.getUserid());
                                    noticeMapper.insert(userNotice);
                                    orderinfo.setTime("2");
                                    orderInfoMapper.updateById(orderinfo);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        map.put("cid",infoUser.getCid());
                                        map.put("version", infoUser.getVersion());
                                        PushtoSingle.push(map);
                                    }
                                    if(!StringUtils.isEmpty(user.getCid())){
                                        result.put("cid",user.getCid());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                } else if (time > 15 && time <= 30&&"2".equals(orderinfo.getTime())) {
                                    /*扣除30%保证金交给订车用户*/


                                    map.put("title",ConstantUtils.LATE);
                                    map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点");
                                    infoNotice.setAddition(ConstantUtils.LATE);
                                    infoNotice.setContent(ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");
                                    infoNotice.setAddtime(DateUtil.now());
                                    infoNotice.setUserid(infoUser.getUserid());
                                    noticeMapper.insert(infoNotice);


                                    result.put("title",ConstantUtils.DRIVERLATE);
                                    result.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系");
                                    userNotice.setAddition(ConstantUtils.DRIVERLATE);
                                    userNotice.setContent(ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                                    userNotice.setAddtime(DateUtil.now());
                                    userNotice.setUserid(user.getUserid());
                                    noticeMapper.insert(userNotice);
                                    orderinfo.setTime("3");
                                    orderInfoMapper.updateById(orderinfo);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        map.put("cid",infoUser.getCid());
                                        map.put("version", infoUser.getVersion());
                                        PushtoSingle.push(map);
                                    }
                                    if(!StringUtils.isEmpty(user.getCid())){
                                        result.put("cid",user.getCid());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }

                                } else if (time > 30 && time <= 60&&"3".equals(orderinfo.getTime())) {
                                    /*扣除50保证金  并把订车用户的钱归还给订车用户*/

                                    map.put("title",ConstantUtils.LATE);
                                    map.put("text",ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点");
                                    infoNotice.setAddition(ConstantUtils.LATE);
                                    infoNotice.setContent(ConstantUtils.LATE+time+"分钟，请尽快到达新人指定地点！");
                                    infoNotice.setAddtime(DateUtil.now());
                                    infoNotice.setUserid(infoUser.getUserid());
                                    noticeMapper.insert(infoNotice);

                                    result.put("title",ConstantUtils.DRIVERLATE);
                                    result.put("text",ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系");
                                    userNotice.setAddition(ConstantUtils.DRIVERLATE);
                                    userNotice.setContent(ConstantUtils.DRIVERLATE+time+"分钟，请与车主联系！");
                                    userNotice.setAddtime(DateUtil.now());
                                    userNotice.setUserid(user.getUserid());
                                    noticeMapper.insert(userNotice);
                                    orderinfo.setTime("4");
                                    orderInfoMapper.updateById(orderinfo);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        map.put("cid",infoUser.getCid());
                                        map.put("version", infoUser.getVersion());
                                        PushtoSingle.push(map);
                                    }
                                    if(!StringUtils.isEmpty(user.getCid())){
                                        result.put("cid",user.getCid());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                } else if(time>60&&"4".equals(orderinfo.getTime())) {
                                    /*扣除100%保证金   并把订车用户的钱归还给订车用户*/

                                    map.put("title","您已缺席");
                                    map.put("text","您缺席，将扣除您100%保证金和用车租金");
                                    infoNotice.setAddition("您已缺席");
                                    infoNotice.setContent("您缺席，将扣除您100%保证金和用车租金！");
                                    infoNotice.setAddtime(DateUtil.now());
                                    infoNotice.setUserid(infoUser.getUserid());
                                    noticeMapper.insert(infoNotice);


                                    result.put("title","车主已缺席");
                                    result.put("text","车主已缺席，车主出车保证金已赔付到您账户！");
                                    userNotice.setAddition("车主已缺席");
                                    userNotice.setContent("车主已缺席，车主出车保证金已赔付到您账户！");
                                    userNotice.setAddtime(DateUtil.now());
                                    userNotice.setUserid(user.getUserid());
                                    noticeMapper.insert(userNotice);
                                    orderinfo.setTime("5");
                                    orderInfoMapper.updateById(orderinfo);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        map.put("cid",infoUser.getCid());
                                        map.put("version", infoUser.getVersion());
                                        PushtoSingle.push(map);
                                    }
                                    if(!StringUtils.isEmpty(user.getCid())){
                                        result.put("cid",user.getCid());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                }

                            }

                        }
                });
            }
        });
       CronUtil.schedule("*/10 * * * *", new Task() {
            @Override
            public void execute() {
                String now = DateUtil.now();
                String today = DateUtil.today();
                /**订单超过24小时佣金退还*/
                /**查询type等于1status等于0orderpaystatus等于1carmainpaystatus等于0*/
                List<Orderinfo> orderinfoListOneDay = orderInfoMapper.selectOrderinfoOneDay();
                orderinfoListOneDay.parallelStream().forEach((orderinfo) -> {
                    Orders order = ordersMapper.selectById(orderinfo.getOrderid());
                    long betweenDay = DateUtil.between(DateUtil.parse(now, "yyyy-MM-dd HH:mm:ss"), DateUtil.parse(orderinfo.getUpdatetime(), "yyyy-MM-dd HH:mm:ss"), DateUnit.MINUTE);
                    if (betweenDay > 30) {
                        /*orderinfo.setOrderpaystatus("0");
                        orderinfo.setType("0");*/
                        User infoUser=userMapper.selectById(orderinfo.getUserid());
                        User user = userMapper.selectById(order.getUserid());
                        user.setPrice(user.getPrice() + orderinfo.getPrice());
                        userMapper.updateById(user);
                        Earn earn = new Earn("退还租金", orderinfo.getPrice(), user.getUserid(), "0", DateUtil.now());
                        earnMapper.insert(earn);
                        orderInfoMapper.deleteById(orderinfo.getOrderinfoid());
                        Notice notice=new Notice();
                        notice.setIsread("0");
                        notice.setAddtime(DateUtil.now());
                        notice.setUserid(order.getUserid());
                        notice.setAddition(ConstantUtils.ORDERCANCEL);
                        notice.setContent("因车主未支付出车保证金您的订单已取消，用车款已退回到您账户！");
                        noticeMapper.insert(notice);
                        if(!StringUtils.isEmpty(user.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",user.getCid());
                            result.put("title",notice.getAddition());
                            result.put("text",notice.getContent());
                            result.put("version", user.getVersion());
                            PushtoSingle.push(result);
                        }

                        Notice notice1=new Notice();
                        notice1.setIsread("0");
                        notice1.setAddtime(DateUtil.now());
                        notice1.setUserid(infoUser.getUserid());
                        notice1.setAddition(ConstantUtils.ORDERCANCEL);
                        notice1.setContent("因您未及时支付出车保证金，您的订单已取消！");
                        noticeMapper.insert(notice1);
                        if(!StringUtils.isEmpty(infoUser.getCid())){
                            Map<String,Object> result=new HashMap<>();
                            result.put("cid",infoUser.getCid());
                            result.put("title",notice1.getAddition());
                            result.put("text",notice1.getContent());
                            result.put("version", infoUser.getVersion());
                            PushtoSingle.push(result);
                        }

                    }
                });
                /**迟到60分钟的订单进行处罚*/
                /**查询进行中未到达的订单*/
                List<Orderinfo> orderinfoLate = orderInfoMapper.selectOrderinfoLate();

                    for(Orderinfo orderinfo:orderinfoLate){
                        Orders order = ordersMapper.selectById(orderinfo.getOrderid());

                        String marraytime = DateUtil.format(DateUtil.parse(order.getMarrytime()), "yyyy-MM-dd");
                        if (!StringUtils.isEmpty(today) && !StringUtils.isEmpty(marraytime)) {
                            if (today.equals(marraytime)) {
                                Integer marryhour = Integer.valueOf(DateUtil.format(DateUtil.parse(order.getMarrytime()), "HH"));
                                Integer todayhour = Integer.valueOf(DateUtil.format(DateUtil.parse(now), "HH"));
                                if (todayhour > marryhour) {
                                    long hour = DateUtil.between(DateUtil.parse(now, "yyyy-MM-dd HH:mm:ss"), DateUtil.parse(order.getMarrytime(), "yyyy-MM-dd HH:mm:ss"), DateUnit.HOUR);
                                    if (hour > 0) {
                                        User user = userMapper.selectById(order.getUserid());
                                        orderinfo.setRealprice(0.00);
                                        orderinfo.setCause("缺席,扣除100%保证金与订车佣金,减少" + (orderinfo.getPrice() + orderinfo.getMargin()) + "元");
                                        user.setPrice(user.getPrice() + orderinfo.getPrice() + orderinfo.getMargin());
                                        //缺席 2
                                        orderinfo.setIs_sign("2");
                                        orderInfoMapper.updateById(orderinfo);
                                        Earn earn = new Earn(ConstantUtils.DRIVERBREACH, orderinfo.getPrice() + orderinfo.getMargin(), order.getUserid(), "0", DateUtil.now());
                                        earnMapper.insert(earn);
                                        userMapper.updateById(user);
                                        Notice infoNotice=new Notice();
                                        infoNotice.setIsread("0");
                                        infoNotice.setAddition("您已缺席");
                                        infoNotice.setContent("您已缺席，已扣除您的出车保证金作为违约金！");
                                        infoNotice.setAddtime(DateUtil.now());
                                        infoNotice.setUserid(orderinfo.getUserid());
                                        noticeMapper.insert(infoNotice);
                                        User infoUser = userMapper.selectById(orderinfo.getUserid());
                                        if(!StringUtils.isEmpty(infoUser.getCid())){
                                            Map<String,Object> result=new HashMap<>();
                                            result.put("cid",infoUser.getCid());
                                            result.put("title",infoNotice.getAddition());
                                            result.put("text",infoNotice.getContent());
                                            result.put("version", infoUser.getVersion());
                                            PushtoSingle.push(result);
                                        }
                                        Notice userNotice=new Notice();
                                        userNotice.setAddition("车主已缺席");
                                        userNotice.setContent("车主已缺席，车主出车保证金已赔付到您账户！");
                                        userNotice.setIsread("0");
                                        userNotice.setAddtime(DateUtil.now());
                                        userNotice.setUserid(user.getUserid());
                                        noticeMapper.insert(userNotice);
                                        if(!StringUtils.isEmpty(user.getCid())){
                                            Map<String,Object> result=new HashMap<>();
                                            result.put("cid",user.getCid());
                                            result.put("title",userNotice.getAddition());
                                            result.put("text",userNotice.getContent());
                                            result.put("version", user.getVersion());
                                            PushtoSingle.push(result);
                                        }

                                        orderInfoMapper.deleteById(orderinfo);
                                        if("1".equals(order.getOrderstatus())){
                                            Integer count = orderInfoMapper.selectUnfinished(order.getOrderid());
                                            if(count<1){
                                                ordersMapper.deleteById(order.getOrderid());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                /**次日凌晨进行中转换为完成*/
                /*List<Orderinfo> orderinfoswitch = orderInfoMapper.selectOrderinfoSwitch();
                orderinfoswitch.parallelStream().forEach((orderinfo) -> {
                    Orders order = ordersMapper.selectById(orderinfo.getOrderid());
                    String marrytime=DateUtil.format(DateUtil.offsetDay(DateUtil.parse(order.getMarrytime()), 1),"yyyy-MM-dd");
                    if(!StringUtils.isEmpty(today)&&!StringUtils.isEmpty(marrytime)){
                        if(today.equals(marrytime)){
                            orderinfo.setType("3");
                            orderinfo.setSettlement("0");
                            orderInfoMapper.updateById(orderinfo);
                            if("1".equals(order.getOrderstatus())){
                                order.setOrderstatus("2");
                            }
                            ordersMapper.updateById(order);
                        }
                    }
                });*/



                /**结婚当天不算结束两天后结算*/
                /*List<Orderinfo> orderinfosettlement = orderInfoMapper.selectOrderinfoSettlement();*/


                List<Orderinfo> orderinfosettlement = orderInfoMapper.selectOrderinfoSwitch();
                orderinfosettlement.parallelStream().forEach((orderinfo)->{
                    Orders order = ordersMapper.selectById(orderinfo.getOrderid());
                    User user=userMapper.selectById(order.getUserid());
                    String marrytime=DateUtil.format(DateUtil.offsetDay(DateUtil.parse(order.getMarrytime()), 3),"yyyy-MM-dd");
                    if(!StringUtils.isEmpty(today)&&!StringUtils.isEmpty(marrytime)){
                        if(today.equals(marrytime)){
                            if("1".equals(orderinfo.getStatus())){
                                if(!StringUtils.isEmpty(orderinfo.getCarid())){
                                    /**增加销量*/
                                    Officialcar officialcar=officialcarMapper.selectById(orderinfo.getCarid());
                                    officialcar.setSales(officialcar.getSales()+1);
                                    officialcarMapper.updateById(officialcar);
                                    /**增加库存*/
                                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(orderinfo.getCarid(),orderinfo.getCarcolor());
                                    carcolor.setInventory(carcolor.getInventory()+1);
                                    carcolorMapper.updateById(carcolor);
                                }
                            }else{
                                /*Car carMargin=carMapper.selectById(orderinfo.getCarid());
                                carMargin.setStarttime(DateUtil.now());
                                carMargin.setEndtime(DateUtil.now());
                                carMapper.updateById(carMargin);*/
                                if(orderinfo.getRealprice()>0){
                                    User infoUser=userMapper.selectById(orderinfo.getUserid());
                                    infoUser.setPrice(infoUser.getPrice()+(orderinfo.getRealprice()-orderinfo.getPrice()*0.2));
                                    userMapper.updateById(infoUser);
                                    Earn earn=new Earn("佣金结算",orderinfo.getRealprice()-orderinfo.getPrice()*0.2,orderinfo.getUserid(),"0",DateUtil.now());
                                    earnMapper.insert(earn);
                                    Notice notice=new Notice();
                                    notice.setAddition(ConstantUtils.ORDERCOMPLETE);
                                    notice.setContent("系统自动结算，请查看钱包金额是否正确！");
                                    notice.setIsread("0");
                                    notice.setAddtime(DateUtil.now());
                                    notice.setUserid(orderinfo.getUserid());
                                    noticeMapper.insert(notice);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        Map<String,Object> result=new HashMap<>();
                                        result.put("cid",infoUser.getCid());
                                        result.put("title",notice.getAddition());
                                        result.put("text",notice.getContent());
                                        result.put("version", infoUser.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                    Notice notice1=new Notice();
                                    notice1.setAddition(ConstantUtils.ORDERCOMPLETE);
                                    notice1.setContent("订单已完成，祝您用车愉快！");
                                    notice1.setIsread("0");
                                    notice1.setAddtime(DateUtil.now());
                                    notice1.setUserid(orderinfo.getUserid());
                                    noticeMapper.insert(notice1);
                                    if(!StringUtils.isEmpty(infoUser.getCid())){
                                        Map<String,Object> result=new HashMap<>();
                                        result.put("cid",user.getCid());
                                        result.put("title",notice1.getAddition());
                                        result.put("text",notice1.getContent());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                }else{

                                    user.setPrice(user.getPrice()+orderinfo.getPrice()+orderinfo.getMargin());
                                    userMapper.updateById(user);
                                    Earn earn=new Earn(ConstantUtils.DRIVERBREACH,orderinfo.getPrice()+orderinfo.getMargin(),user.getUserid(),"0",DateUtil.now());
                                    earnMapper.insert(earn);
                                    Notice notice=new Notice();
                                    notice.setAddition(ConstantUtils.ORDERCOMPLETE);
                                    notice.setContent("系统自动结算，车主违约金将赔付到您的钱包，请尽快查收！");
                                    notice.setIsread("0");
                                    notice.setAddtime(DateUtil.now());
                                    notice.setUserid(user.getUserid());
                                    noticeMapper.insert(notice);
                                    if(!StringUtils.isEmpty(user.getCid())){
                                        Map<String,Object> result=new HashMap<>();
                                        result.put("cid",user.getCid());
                                        result.put("title",notice.getAddition());
                                        result.put("text",notice.getContent());
                                        result.put("version", user.getVersion());
                                        PushtoSingle.push(result);
                                    }
                                }
                                orderinfo.setSettlement("1");


                            }
                            orderinfo.setType("3");
                            orderInfoMapper.updateById(orderinfo);
                            order.setOrderstatus("2");
                            ordersMapper.updateById(order);
                        }
                    }

                });

            }
           });
        CronUtil.schedule("50 23 * * *", new Task() {
            @Override
            public void execute() {
                    //删除和当前时间一下的婚礼日期
                List<Orders> orders = ordersMapper.selectAfterMarry();
                String today=DateUtil.today();
                for (Orders order:orders){
                    String marrytime = order.getMarrytime();
                    String marray = DateUtil.format(DateUtil.parse(marrytime, "yyyy-MM-dd"), "yyyy-MM-dd");
                    if(marray.equals(today)){
                        ordersMapper.deleteById(order);
                        orderInfoMapper.deleteByOrderId(order.getOrderid());
                    }
                }
            }
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    @Override
    public boolean checkinventory(List<Map<String,Object>> carList) {
        boolean is_success=true;

        try {
            for (Map<String,Object> map:carList){
                String carid=map.get("carid")==null?null:map.get("carid").toString();
                String color=map.get("color")==null?null:map.get("color").toString();
                Integer number=map.get("number")==null?null:(Integer)map.get("number");
                Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(carid,color);
                if(Integer.valueOf(carcolor.getInventory())<number){
                    is_success=false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is_success;
    }

    @Override
    public void carAbsent( String orderid) {
        if(!StringUtils.isEmpty(orderid)){
            Orders order=ordersMapper.selectById(orderid);
            User user=userMapper.selectById(order.getUserid());

            /**查询我的所有车辆订单*/
            List<Orderinfo> orderinfoList=orderInfoMapper.selectOrderinfoByUseridAndOrderid(null,orderid);
            Double price=user.getPrice();
            orderinfoList.parallelStream().forEach((orderinfo)->{
                if("0".equals(orderinfo.getIs_sign())){
                    orderinfo.setRealprice(0.00);
                    orderinfo.setCause("缺席,扣除100%保证金与订车佣金,减少"+(orderinfo.getPrice()+orderinfo.getMargin())+"元");
                    user.setPrice(user.getPrice()+orderinfo.getPrice()+orderinfo.getMargin());
                    orderInfoMapper.updateById(orderinfo);
                }
            });
            price=user.getPrice()-price;
            if(price>0){
                Earn earn=new Earn(ConstantUtils.DRIVERBREACH,price,order.getUserid(),"0",DateUtil.now());
                earnMapper.insert(earn);
                userMapper.updateById(user);
            }
        }
    }

    @Override
    public void orderAutoComplete(String orderid) {

        if(!StringUtils.isEmpty(orderid)){
            Orders order=ordersMapper.selectById(orderid);
            List<Orderinfo> orderinfoList=orderInfoMapper.selectOrderinfoCompleteByOrderid(orderid);



                for (Orderinfo orderinfo:orderinfoList){
                    orderinfo.setType("3");
                    orderinfo.setSettlement("1");
                    /**增加销量*/
                    Officialcar officialcar=officialcarMapper.selectById(orderinfo.getCarid());
                    officialcar.setSales(officialcar.getSales()+1);
                    System.out.println(officialcar);
                    officialcarMapper.updateById(officialcar);
                    /**增加库存*/
                    Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(orderinfo.getCarid(),orderinfo.getCarcolor());
                    carcolor.setInventory(carcolor.getInventory()+1);
                    System.out.println(carcolor);
                    carcolorMapper.updateById(carcolor);

                    orderInfoMapper.updateById(orderinfo);
                }

            order.setOrderstatus("2");

            ordersMapper.updateById(order);
        }
    }

    @Override
    public void orderManualComplete(String orderinfoid) {

        if(!StringUtils.isEmpty(orderinfoid)){
            Orderinfo orderinfo= orderInfoMapper.selectById(orderinfoid);
            Orders order=ordersMapper.selectById(orderinfo.getOrderid());
            User user=userMapper.selectById(order.getUserid());
            orderinfo.setType("3");
            orderinfo.setSettlement("1");
            if(orderinfo.getRealprice()>0){
                User infoUser=userMapper.selectById(orderinfo.getUserid());
                infoUser.setPrice(infoUser.getPrice()+orderinfo.getRealprice()-orderinfo.getPrice()*0.2);
                userMapper.updateById(infoUser);
                Earn earn=new Earn("佣金结算",orderinfo.getRealprice()-orderinfo.getPrice()*0.2,orderinfo.getUserid(),"0",DateUtil.now());
                earnMapper.insert(earn);
            }else{
                //运行
                user.setPrice(user.getPrice()+orderinfo.getPrice()+orderinfo.getMargin());
                userMapper.updateById(user);
                Earn earn=new Earn(ConstantUtils.DRIVERBREACH,orderinfo.getPrice()+orderinfo.getMargin(),user.getUserid(),"0",DateUtil.now());
                earnMapper.insert(earn);
            }
            orderinfo.setSettlement("1");
            orderInfoMapper.updateById(orderinfo);
            Notice notice=new Notice();
            notice.setAddtime(DateUtil.now());
            notice.setAddition(ConstantUtils.ORDERCOMPLETE);
            notice.setContent("您的订单到达已完成状态，请检查钱包金额是否正确！");

            User infoUser=userMapper.selectById(orderinfo.getUserid());
            if(!StringUtils.isEmpty(infoUser.getCid())){
                Map<String,Object> result=new HashMap<>();
                result.put("cid",infoUser.getCid());
                result.put("title",notice.getAddition());
                result.put("text",notice.getContent());
                result.put("version", infoUser.getVersion());
                PushtoSingle.push(result);
            }

            notice.setIsread("0");
            notice.setUserid(orderinfo.getUserid());
            noticeMapper.insert(notice);
            Integer count=orderInfoMapper.selectUnfinished(orderinfo.getOrderid());
            if(count<1){

                order.setOrderstatus("2");
                ordersMapper.updateById(order);
                List<Orderinfo> orderinfoList=orderInfoMapper.selectOngoing(order.getOrderid());
                orderinfoList.parallelStream().forEach((orderInfo)->{
                    Car carMargin=carMapper.selectById(orderInfo.getCarid());
                    carMargin.setStarttime(DateUtil.now());
                    carMargin.setEndtime(DateUtil.now());
                    carMapper.updateById(carMargin);
                });
            }

        }
    }

    @Override
    public List<Map<String, Object>> reserveCarOrders(Map<String, Object> map) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(map!=null&&!map.isEmpty()){
            listMap=orderInfoMapper.reserveCarOrders(map);
        }
        return listMap;
    }

    @Override
    public void orderCompleteSettlement(List<String> orderinfoidList) {

        orderinfoidList.parallelStream().forEach((orderinfoid)-> {
            Orderinfo orderinfo=orderInfoMapper.selectById(orderinfoid);
            User user=userMapper.selectById(orderinfo.getUserid());
            user.setPrice(user.getPrice()+orderinfo.getRealprice());
            userMapper.updateById(user);
            Earn earn=new Earn("佣金结算",orderinfo.getRealprice(),orderinfo.getUserid(),"0",DateUtil.now());
            earnMapper.insert(earn);

            orderinfo.setSettlement("1");
            orderInfoMapper.updateById(orderinfo);
        });

    }

    @Override
    public void orderComplete(String orderid) {
        if(!StringUtils.isEmpty(orderid)){
           Orders order= ordersMapper.selectById(orderid);
           order.setOrderstatus("2");
           ordersMapper.updateById(order);
            List<Orderinfo> orderinfoList=orderInfoMapper.selectOrderInfoByOrderIdAndStatus(orderid,"1");
            orderinfoList.parallelStream().forEach((orderinfo)->{
                Officialcar officialcar=officialcarMapper.selectById(orderinfo.getCarid());
                officialcar.setSales(officialcar.getSales()+1);
                officialcarMapper.updateById(officialcar);
                /**增加库存*/
                Carcolor carcolor=carcolorMapper.selectCarColorByCaridAndColor(orderinfo.getCarid(),orderinfo.getCarcolor());
                carcolor.setInventory(carcolor.getInventory()+1);
                carcolorMapper.updateById(carcolor);
            });
        }
    }

    @Override
    public boolean isBeyondRes(String orderinfoid) {
        boolean isbeyond=false;
        if(!StringUtils.isEmpty(orderinfoid)){
            Orderinfo orderinfo=orderInfoMapper.selectById(orderinfoid);
            Orders order=ordersMapper.selectById(orderinfo.getOrderid());

            if("main".equals(orderinfo.getCartype())){
                Integer number=orderInfoMapper.selectOrderinfoByResNumber(order.getOrderid(),"main");
                if(number<1){
                    isbeyond=true;
                }
            }else{
                Integer number=orderInfoMapper.selectOrderinfoByResNumber(order.getOrderid(),"deputy");
                if(number<order.getDeputycarnum()){
                    isbeyond=true;
                }
            }
        }
        return isbeyond;
    }

    @Override
    public List<Map<String, Object>> payorderinfo(String orderid, String userid,String orderpaystatus) {
        List<Map<String, Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(orderid)&&!StringUtils.isEmpty(userid)){
            Orderinfo orderinfo=new Orderinfo();
            orderinfo.setUserid(userid);
            orderinfo.setOrderid(orderid);
            orderinfo.setOrderpaystatus(orderpaystatus);
            listMap=orderInfoMapper.payorderinfo(orderinfo);
        }
        return listMap;
    }

    @Override
    public List<Car> selectCarByNotMarrayTime(String marraytime, String hour,String userid) {
        List<Car> carList=new ArrayList<>();
        if(!StringUtils.isEmpty(marraytime)&&!StringUtils.isEmpty(hour)&&!StringUtils.isEmpty(userid)){
            String endtime=DateUtil.format(DateUtil.offsetHour(DateUtil.parse(marraytime, "yyyy-MM-dd HH:mm:ss"), Integer.valueOf(hour)),"yyyy-MM-dd HH:mm:ss");
            Map<String,Object> map=new HashMap<>();
            map.put("starttime",marraytime);
            map.put("endtime",endtime);
            map.put("userid",userid);
            carList=carMapper.selectCarByNotMarrayTime(map);
        }
        return carList;
    }

    @Override
    public boolean addAsset(Asset asset) {
        boolean success=true;
        if(asset!=null){
            User user=userMapper.selectById(asset.getUserid());
            if(asset.getPrice()>user.getPrice()){
                success=false;
            }else{
                asset.setStatus("0");
                asset.setAddtime(DateUtil.now());
                asset.setUpdatetime(DateUtil.now());
                assetMapper.insert(asset);

                user.setPrice(user.getPrice()-asset.getPrice());
                userMapper.updateById(user);
                Earn earn=new Earn("提现",asset.getPrice(),user.getUserid(),"1",DateUtil.now());
                earnMapper.insert(earn);
            }
        }
        return success;
    }

    @Override
    public List<User> selectUserList(String phone) {

        return userMapper.selectUserList(phone);
    }

    @Override
    public User selectUserByUserId(String userid) {
        User user=null;
        if(!StringUtils.isEmpty(userid)){
            user=userMapper.selectById(userid);
        }
        return user;
    }



    private static String reach(String marrytime, String cause){
        Integer marry=Integer.valueOf(DateUtil.format(DateUtil.parse(marrytime),"HH"));
        Integer today= Integer.valueOf(DateUtil.format(new Date(),"HH"));
        if(today>=marry){
            /**未迟到*/
            long time=DateUtil.between(new Date(),DateUtil.parse(marrytime), DateUnit.MINUTE);
            if(time>0&&time<=15){
                /**发送警告*/
            }else if(time>15&&time<=30){
                /**扣除30%保证金交给订车用户*/
            }else if(time>30&&time<=60){
                /**扣除50保证金  并把订车用户的钱归还给订车用户*/
            }else{

            }
        }
        return cause;
    }
    public static void main(String[] args){
        reach("2020-01-10 14:54:22","原因");
    }
    private static Integer screen(Double price){

        Integer margin=null;
        if(price<1000){
            margin=100;
        }else if(price>=1000&&price<2000){
            margin=200;
        }else if(price>=2000&&price<5000){
            margin=500;
        }else{
            margin=1000;
        }
        return margin;
    }


}
