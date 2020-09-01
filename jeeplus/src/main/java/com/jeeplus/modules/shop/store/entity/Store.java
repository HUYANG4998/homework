/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.store.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import javax.persistence.Transient;

/**
 * 商家管理Entity
 * @author lhh
 * @version 2019-12-20
 */
public class Store extends DataEntity<Store> {

	private static final long serialVersionUID = 1L;
	private String name;		// 商家名称
	private String phone;		// 联系方式
	private String pwd;		// 密码
	private String money;		// 余额
	private String baoMoney;		// 保证金
	private String img;		// 头像
	private String weiOpenId;		// 微信openid
	private String qqId;		// qqopneid
	private String isRen;		// 是否认证
	private String isShen;		// 是否审核
	private String realName;		// 真实姓名
	private String card;		// 身份证号
	private String cardZ;		// 身份证正面
	private String cardF;		// 身份证反面
	private String cardS;		// 手持身份证
	private String menImg;		// 门头照片
	private String neiImg;		// 店内照片
	private String yingImg;		// 营业执照照片
	private String hangImg;		// 行业执照
	private String pinImg;		// 品牌授权书
	private String address;		// 商家地址
	private String addressDetail;		// 商家详细地址
	private String lng;		// 商家经度
	private String lat;		// 商家纬度
	private String starNum;		// 星级数
	private String fenNum;		// 粉丝数
	private String byOne;		// 备用1
	private String byTwo;		// 备用2
	private String byThree;		// 备用3

	private String isFllow;//前端专用 判断是否已经关注过

	private String isUser;

	private Integer num;//微信支付数量

	public Store() {
		super();
	}

	public Store(String id){
		super(id);
	}

	@ExcelField(title="商家名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ExcelField(title="联系方式", align=2, sort=2)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@ExcelField(title="密码", align=2, sort=3)
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@ExcelField(title="余额", align=2, sort=4)
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	@ExcelField(title="保证金", align=2, sort=5)
	public String getBaoMoney() {
		return baoMoney;
	}

	public void setBaoMoney(String baoMoney) {
		this.baoMoney = baoMoney;
	}

	@ExcelField(title="头像", align=2, sort=6)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@ExcelField(title="微信openid", align=2, sort=7)
	public String getWeiOpenId() {
		return weiOpenId;
	}

	public void setWeiOpenId(String weiOpenId) {
		this.weiOpenId = weiOpenId;
	}

	@ExcelField(title="qqopneid", align=2, sort=8)
	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	@ExcelField(title="是否认证", dictType="yes_no", align=2, sort=10)
	public String getIsRen() {
		return isRen;
	}

	public void setIsRen(String isRen) {
		this.isRen = isRen;
	}

	@ExcelField(title="是否审核", dictType="yes_no", align=2, sort=11)
	public String getIsShen() {
		return isShen;
	}

	public void setIsShen(String isShen) {
		this.isShen = isShen;
	}

	@ExcelField(title="真实姓名", align=2, sort=12)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@ExcelField(title="身份证号", align=2, sort=13)
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	@ExcelField(title="身份证正面", align=2, sort=14)
	public String getCardZ() {
		return cardZ;
	}

	public void setCardZ(String cardZ) {
		this.cardZ = cardZ;
	}

	@ExcelField(title="身份证反面", align=2, sort=15)
	public String getCardF() {
		return cardF;
	}

	public void setCardF(String cardF) {
		this.cardF = cardF;
	}

	@ExcelField(title="手持身份证", align=2, sort=16)
	public String getCardS() {
		return cardS;
	}

	public void setCardS(String cardS) {
		this.cardS = cardS;
	}

	@ExcelField(title="门头照片", align=2, sort=17)
	public String getMenImg() {
		return menImg;
	}

	public void setMenImg(String menImg) {
		this.menImg = menImg;
	}

	@ExcelField(title="店内照片", align=2, sort=18)
	public String getNeiImg() {
		return neiImg;
	}

	public void setNeiImg(String neiImg) {
		this.neiImg = neiImg;
	}

	@ExcelField(title="营业执照照片", align=2, sort=19)
	public String getYingImg() {
		return yingImg;
	}

	public void setYingImg(String yingImg) {
		this.yingImg = yingImg;
	}

	@ExcelField(title="行业执照", align=2, sort=20)
	public String getHangImg() {
		return hangImg;
	}

	public void setHangImg(String hangImg) {
		this.hangImg = hangImg;
	}

	@ExcelField(title="品牌授权书", align=2, sort=21)
	public String getPinImg() {
		return pinImg;
	}

	public void setPinImg(String pinImg) {
		this.pinImg = pinImg;
	}

	@ExcelField(title="商家地址", align=2, sort=22)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ExcelField(title="商家详细地址", align=2, sort=23)
	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	@ExcelField(title="商家经度", align=2, sort=24)
	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	@ExcelField(title="商家纬度", align=2, sort=25)
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@ExcelField(title="星级数", align=2, sort=26)
	public String getStarNum() {
		return starNum;
	}

	public void setStarNum(String starNum) {
		this.starNum = starNum;
	}

	@ExcelField(title="粉丝数", align=2, sort=27)
	public String getFenNum() {
		return fenNum;
	}

	public void setFenNum(String fenNum) {
		this.fenNum = fenNum;
	}

	@ExcelField(title="备用1", align=2, sort=30)
	public String getByOne() {
		return byOne;
	}

	public void setByOne(String byOne) {
		this.byOne = byOne;
	}

	@ExcelField(title="备用2", align=2, sort=31)
	public String getByTwo() {
		return byTwo;
	}

	public void setByTwo(String byTwo) {
		this.byTwo = byTwo;
	}

	@ExcelField(title="备用3", align=2, sort=32)
	public String getByThree() {
		return byThree;
	}

	public void setByThree(String byThree) {
		this.byThree = byThree;
	}
	@Transient
	public String getIsFllow() {
		return isFllow;
	}

	public void setIsFllow(String isFllow) {
		this.isFllow=isFllow;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
