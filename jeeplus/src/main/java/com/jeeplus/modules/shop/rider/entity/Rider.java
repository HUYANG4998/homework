/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.rider.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 骑手管理Entity
 * @author lhh
 * @version 2020-01-16
 */
public class Rider extends DataEntity<Rider> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 骑手名称
	private String img;		// 头像
	private String phone;		// 联系方式
	private String money;		// 余额
	private String weiOpenid;		// 微信openid
	private String qqId;		// qqopenid
	private String yesNO;		// 是否在线
	private String starNum;		// 星级数
	private String realName;		// 真实姓名
	private String card;		// 身份证号
	private String cardZ;		// 身份证正面
	private String cardF;		// 身份证反面
	private String cardS;		// 手持身份证
	private String isRen;		// 是否认证
	private String isShen;		// 是否审核
	private String isYou;		// 是否有订单
	private String lng;		// 经度
	private String lat;		// 纬度
	private String isUser;
	
	public Rider() {
		super();
	}

	public Rider(String id){
		super(id);
	}

	@ExcelField(title="骑手名称", align=2, sort=5)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="头像", align=2, sort=6)
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	@ExcelField(title="联系方式", align=2, sort=7)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="余额", align=2, sort=8)
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	@ExcelField(title="微信openid", align=2, sort=9)
	public String getWeiOpenid() {
		return weiOpenid;
	}

	public void setWeiOpenid(String weiOpenid) {
		this.weiOpenid = weiOpenid;
	}
	
	@ExcelField(title="qqopenid", align=2, sort=10)
	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}
	
	@ExcelField(title="是否在线", dictType="yes_no", align=2, sort=11)
	public String getYesNO() {
		return yesNO;
	}

	public void setYesNO(String yesNO) {
		this.yesNO = yesNO;
	}
	
	@ExcelField(title="星级数", align=2, sort=12)
	public String getStarNum() {
		return starNum;
	}

	public void setStarNum(String starNum) {
		this.starNum = starNum;
	}
	
	@ExcelField(title="真实姓名", align=2, sort=13)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@ExcelField(title="身份证号", align=2, sort=14)
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}
	
	@ExcelField(title="身份证正面", align=2, sort=15)
	public String getCardZ() {
		return cardZ;
	}

	public void setCardZ(String cardZ) {
		this.cardZ = cardZ;
	}
	
	@ExcelField(title="身份证反面", align=2, sort=16)
	public String getCardF() {
		return cardF;
	}

	public void setCardF(String cardF) {
		this.cardF = cardF;
	}
	
	@ExcelField(title="手持身份证", align=2, sort=17)
	public String getCardS() {
		return cardS;
	}

	public void setCardS(String cardS) {
		this.cardS = cardS;
	}
	
	@ExcelField(title="是否认证", dictType="yes_no", align=2, sort=18)
	public String getIsRen() {
		return isRen;
	}

	public void setIsRen(String isRen) {
		this.isRen = isRen;
	}
	
	@ExcelField(title="是否审核", dictType="yes_no", align=2, sort=19)
	public String getIsShen() {
		return isShen;
	}

	public void setIsShen(String isShen) {
		this.isShen = isShen;
	}
	
	@ExcelField(title="是否有订单", dictType="yes_no", align=2, sort=21)
	public String getIsYou() {
		return isYou;
	}

	public void setIsYou(String isYou) {
		this.isYou = isYou;
	}
	
	@ExcelField(title="经度", align=2, sort=23)
	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}
	
	@ExcelField(title="纬度", align=2, sort=24)
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getIsUser() {
		return isUser;
	}

	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}
}