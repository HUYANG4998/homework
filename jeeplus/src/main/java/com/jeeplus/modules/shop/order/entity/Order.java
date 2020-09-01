/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.order.entity;

import com.jeeplus.modules.shop.customer.entity.Customer;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.shop.store.entity.Store;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.express.entity.Express;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.persistence.Transient;

/**
 * 订单管理Entity
 * @author lhh
 * @version 2020-01-16
 */
public class Order extends DataEntity<Order> {

	private static final long serialVersionUID = 1L;
	private String numbers;		// 订单号
	private String totalPrice;		// 总价格
	private String payType;		// 支付方式
	private Customer customer;		// 用户名称
	private CustomerAddress customerAddress;		// 用户地址
	private Date payTime;		// 支付时间
	private String peiType;		// 配送方式
	private String orderState;		// 订单交易状态（0:待付款 1:已付款 2:待收货 3:已收货4:待评价5:已评价6:取消）
	private String count;		// 商品个数（共几件商品）
	private String storeState;		// 商家状态（0:未接单1:已接单2:配货完成3:待发货4:已发货）
	private String riderState;		// 骑手状态（0:待接单1:已接单2:待配送3:配送完成）
	private Store store;		// 店铺名称
	private Rider rider;		// 骑手名称
	private String riderJieTime;		// 骑手接单时间
	private String songTime;		// 送达时间
	private String storeDiscount;		// 商家优惠
	private String waresDiscount;		// 商品优惠
	private String storeDiscountDetail;		// 商家优惠详细(满多少金额送xx)
	private String waresDiscountDetail;		// 商品优惠详细(满多少金额送xx)
	private String totalPei;		// 总配送费
	private String byOne;		// 总商品价格
	private String byTwo;		// 订单结束时间
	private String byThree;		// 取消订单原因
	private Integer pickCode;		// 取件码
	private Express express;		// 快递方式
	private String expressNumbers;		// 快递单号
	private String totalDistance;		// 总配送距离
	private Double shangJu;//骑手距商家的距离
	private Double yongJu;//骑手距用户的距离
	private List<OrderDetail> orderDetailList = Lists.newArrayList();		// 子表列表
	private String status; //前端修改状态
	private String audit; //退款状态0审核中1通过2拒绝
	private String deliverytime;//发货时间
	private String ridersettlement;

	public Order() {
		super();
	}

	public Order(String id){
		super(id);
	}

	@ExcelField(title="订单号", align=2, sort=5)
	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	@ExcelField(title="总价格", align=2, sort=6)
	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	@ExcelField(title="支付方式", align=2, sort=7)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@ExcelField(title="用户名称", fieldType=Customer.class, value="customer.name", align=2, sort=8)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ExcelField(title="用户地址", fieldType=CustomerAddress.class, value="customerAddress.address", align=2, sort=9)
	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="支付时间", align=2, sort=10)
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@ExcelField(title="配送方式", dictType="pei_type", align=2, sort=11)
	public String getPeiType() {
		return peiType;
	}

	public void setPeiType(String peiType) {
		this.peiType = peiType;
	}

	@ExcelField(title="订单交易状态（0:待付款 1:已付款 2:待收货 3:已收货4:待评价5:已评价6:取消）", dictType="order_state", align=2, sort=12)
	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	@ExcelField(title="商品个数（共几件商品）", align=2, sort=13)
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@ExcelField(title="商家状态（0:未接单1:已接单2:配货完成3:待发货4:已发货）", dictType="store_state", align=2, sort=14)
	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}

	@ExcelField(title="骑手状态（0:待接单1:已接单2:待配送3:配送完成）", align=2, sort=15)
	public String getRiderState() {
		return riderState;
	}

	public void setRiderState(String riderState) {
		this.riderState = riderState;
	}

	@ExcelField(title="店铺名称", fieldType=Store.class, value="store.name", align=2, sort=17)
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	@ExcelField(title="骑手名称", fieldType=Rider.class, value="rider.name", align=2, sort=18)
	public Rider getRider() {
		return rider;
	}

	public void setRider(Rider rider) {
		this.rider = rider;
	}

	@ExcelField(title="骑手接单时间", align=2, sort=19)
	public String getRiderJieTime() {
		return riderJieTime;
	}

	public void setRiderJieTime(String riderJieTime) {
		this.riderJieTime = riderJieTime;
	}

	@ExcelField(title="送达时间", align=2, sort=20)
	public String getSongTime() {
		return songTime;
	}

	public void setSongTime(String songTime) {
		this.songTime = songTime;
	}

	@ExcelField(title="商家优惠", align=2, sort=21)
	public String getStoreDiscount() {
		return storeDiscount;
	}

	public void setStoreDiscount(String storeDiscount) {
		this.storeDiscount = storeDiscount;
	}

	@ExcelField(title="商品优惠", align=2, sort=22)
	public String getWaresDiscount() {
		return waresDiscount;
	}

	public void setWaresDiscount(String waresDiscount) {
		this.waresDiscount = waresDiscount;
	}

	@ExcelField(title="商家优惠详细(满多少金额送xx)", align=2, sort=23)
	public String getStoreDiscountDetail() {
		return storeDiscountDetail;
	}

	public void setStoreDiscountDetail(String storeDiscountDetail) {
		this.storeDiscountDetail = storeDiscountDetail;
	}

	@ExcelField(title="商品优惠详细(满多少金额送xx)", align=2, sort=24)
	public String getWaresDiscountDetail() {
		return waresDiscountDetail;
	}

	public void setWaresDiscountDetail(String waresDiscountDetail) {
		this.waresDiscountDetail = waresDiscountDetail;
	}

	@ExcelField(title="总配送费", align=2, sort=25)
	public String getTotalPei() {
		return totalPei;
	}

	public void setTotalPei(String totalPei) {
		this.totalPei = totalPei;
	}

	@ExcelField(title="总商品价格", align=2, sort=26)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}

	@ExcelField(title="订单结束时间", align=2, sort=27)
	public String getByTwo() {
		return byTwo;
	}

	public void setByTwo(String byTwo) {
		this.byTwo = byTwo;
	}

	@ExcelField(title="取消订单原因", align=2, sort=28)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}

	@ExcelField(title="取件码", align=2, sort=29)
	public Integer getPickCode() {
		return pickCode;
	}

	public void setPickCode(Integer pickCode) {
		this.pickCode = pickCode;
	}

	@ExcelField(title="快递方式", fieldType=Express.class, value="express.name", align=2, sort=30)
	public Express getExpress() {
		return express;
	}

	public void setExpress(Express express) {
		this.express = express;
	}

	@ExcelField(title="快递单号", align=2, sort=31)
	public String getExpressNumbers() {
		return expressNumbers;
	}

	public void setExpressNumbers(String expressNumbers) {
		this.expressNumbers = expressNumbers;
	}

	@ExcelField(title="总配送距离", align=2, sort=32)
	public String getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}

	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	@Transient
	public Double getShangJu() {
		return shangJu;
	}

	public void setShangJu(Double shangJu) {
		this.shangJu=shangJu;
	}

	@Transient
	public Double getYongJu() {
		return yongJu;
	}

	public void setYongJu(Double yongJu) {
		this.yongJu=yongJu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

	public String getRidersettlement() {
		return ridersettlement;
	}

	public void setRidersettlement(String ridersettlement) {
		this.ridersettlement = ridersettlement;
	}
}
