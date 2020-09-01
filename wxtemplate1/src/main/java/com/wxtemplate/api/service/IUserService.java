package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.*;
import com.wxtemplate.api.entity.VO.OwnerCertVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.tools.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface IUserService extends IService<User> {
    User findUser(String userid);

    void updateUser(User user);

    void authentication(OwnerCertVo vo);

    User findUserByPhone(String phone);

    void addUser(String phone, String password);

    void updateUser(String phone, String newPassword);

    void license(License license);

    void AuthenticationCar(Car car, String carPhotos);
    void deleteCar(String carid);

    Map<String, Object> getAuthentication(String userid);
    /**发布订单*/
    void createOrder(Orders orders);
    /**查询最新订单   发布订单之后页面*/
    Map<String,Object> selectNewOrderDetail(String userid);
    /**抢单*/
    void addMyOrderInfo(List<Orderinfo> orderinfoList);
    /**抢单之后订单详情*/
    Map<String,Object> selectMyOrderInfoDetail(String orderid);

    List<Map<String, Object>> selectMyOrderCar(String orderid, String userid,String type);

    List<Map<String, Object>> order(String orderstatus, String userid);

    List<Map<String, Object>> contactDriver(Map<String,Object> map);

    List<Map<String, Object>> orderViewCarInfo(Map<String,Object> map);

    List<Trip> selectTrip(String userid);

    Trip selectTripByTripId(String tripid);

    void updateTripByTripId(Trip trip);

    void addTrip(Trip trip,String userid);

    void deleteTrip(String tripid);

    void addGarag(String userid, Map<String,Object> map);

    void deleteGarag(List<String> garageidList);

    List<Map<String,Object>> selectGarag(String userid);

    List<Map<String, Object>> selectOrderByOrderInfo(Map<String, Object> map);

    Map<String, Object> selectOrdersByOrderId(String orderid);

    Map<String,Object> reservationPay(Orders order ,List<Map<String,Object>> carList,boolean is_pay_success,boolean is_pay,boolean is_chat);

    List<Map<String, Object>> selectTakeMyOrderInfo(Map<String, Object> map);

    boolean myReleasePayOrderInfo(String orderinfoid,String userid);

    boolean takeMyOrderInfoMargin(String orderinfoid, String userid);

    void cancelAllOrder(String orderid,String userid);

    void cancelAllOrderinfo(String orderid, String userid);

    void cancelOrderinfo(String orderinfoid);

    void cancelOrderinfoReserve(String orderid, String type,List<Map<String,Object>> carList,String userid);

    void releaseEval(Commentuser commentuser, String userid);

    List<Map<String, Object>> myEval(String userid);

    void myFeedback(String userid, Fendback fendback);

    List<Earn> selectEarnByAddtime(String userid, String addtime);

    void addAccount(String userid, Account account);

    void updateAccount(Account asset);

    Account selectAccount(String userid, String ditch);

    Map<String,Object> yesterdayEarnAndprice(String userid,String day);

    User selectUser(String userid);

    Result carReach(String userid, String orderid );

    void carAbsent(String orderid);

    void orderAutoComplete(String orderinfoid);

    void orderCompleteSettlement(List<String> orderinfoidList);

    List<Car> selectCarByNotMarrayTime(String marraytime, String hour,String userid);

    boolean addAsset(Asset asset);

    void extendsTime();

    boolean checkinventory(List<Map<String,Object>> carList);

    List<Map<String,Object>> getorderinfo(String orderid,String type);

    List<User> selectUserList(String phone);

    User selectUserByUserId(String userid);



    void orderComplete(String orderid);

    boolean isBeyondRes(String orderinfoid);

    List<Map<String, Object>> payorderinfo(String orderid, String userid,String orderpaystatus);

    void orderManualComplete(String orderinfoid);


    List<Map<String, Object>> reserveCarOrders(Map<String, Object> map);

    boolean balancePay(Orders order, List<Map<String, Object>> carList);

    void changeStatus(String out_trade_no);

    void detectionOrder(String orderinfoid);

    Map<String,Object> pay(String orderinfoid,boolean pay);

    boolean selectIsSign(String userid,String orderid);
}
