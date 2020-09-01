package com.wxtemplate.wxtemplate.api.util;

import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static java.math.BigDecimal.ROUND_FLOOR;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class Utils {
	/**八位随机数*/
	public static String getRandomEight(){
		String random=(int)((Math.random()*9+1)*10000000)+"";
	   return random;
	}
	/**六位随机数*/
	public static String getRandomSix(){
		String random=(int)((Math.random()*9+1)*100000)+"";
	   return random;
	}
	/**四位随机数*/
	public static String getRandomFour(){
		String random=(int)((Math.random()*9+1)*1000)+"";
	   return random;
	}
	public static SqlSession openSession() {
		SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
		return sqlSessionFactory.openSession();
	}
	/**
	 * 随机12位
	 */
	public static String getRandomTwelve(){
		return String.valueOf(new Date().getTime()-1300000000000L);
	}
	/**生成随机邀请码*/
	public static String createShareCode() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
	}

	public static String getStickAndHot(boolean hotFlag,boolean stickFlag){
		String success="4";
		if(hotFlag&&stickFlag){
			success="1";
		}else if(hotFlag){
			success="2";
		}else if(stickFlag){
			success="3";
		}
		return success;
	}



	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-","");
	}
	private static RedisUtils redisUtils=new RedisUtils();


	public static final int MACHINE_ID = 1;

	/**
	 * 生成订单号
	 */
	public static String getOrderNumber() {
		int machineId = MACHINE_ID;
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		System.out.println("hashCodeV：" + hashCodeV);
		if (hashCodeV < 0) {
			hashCodeV = -hashCodeV;
		}
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正整型
		//edit---20181203 hzj
		Date date=new Date();
		return machineId + String.format("%015d", hashCodeV);
	}
	public static void main(String[] args){
		//红包--start
		Dynamic dynamic=new Dynamic();
		dynamic.setRedPackageNumber(10);
		dynamic.setRedPackageMoney(new BigDecimal(100).setScale(2));

		for (int i=0;i<10;i++){
			BigDecimal randomMoney = RedPackageUtils.getRandomMoney(dynamic);

			System.out.println(randomMoney);
		}
		System.out.println("红包结束");
		//红包--end
		BigDecimal a=new BigDecimal("255");
		BigDecimal b=new BigDecimal("255");
		if(a.compareTo(b) == -1){
			System.out.println("a小于b");
		}

		if(a.compareTo(b) == 0){
			System.out.println("a等于b");
		}

		if(a.compareTo(b) == 1){
			System.out.println("a大于b");
		}

		if(a.compareTo(b) > -1){
			System.out.println("a大于等于b");
		}

		if(a.compareTo(b) < 1){
			System.out.println("a小于等于b");
		}


		//加法
		a.add(b);


		//减法
		a.subtract(b);


		//乘法
		a.multiply(b);


		//除法
		a.divide(b);

		a.setScale(2); // 表示保留两位小数，默认用四舍五入方式





	}
	static class  RedPackage implements Serializable {
		private Integer remainSize;
		private Double remainMoney;

		public Integer getRemainSize() {
			return remainSize;
		}

		public void setRemainSize(Integer remainSize) {
			this.remainSize = remainSize;
		}

		public Double getRemainMoney() {
			return remainMoney;
		}

		public void setRemainMoney(Double remainMoney) {
			this.remainMoney = remainMoney;
		}
	}


	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}



}

