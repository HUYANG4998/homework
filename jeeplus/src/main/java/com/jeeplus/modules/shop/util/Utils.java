package com.jeeplus.modules.shop.util;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.jeeplus.modules.shop.api.chat.entity.Chat;
import net.oschina.j2cache.redis.RedisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
	/**
	 * 八位随机数
	 */
	public static String getRandomEight() {
		String random = (int) ((Math.random() * 9 + 1) * 10000000) + "";
		return random;
	}

	/**
	 * 六位随机数
	 */
	public static String getRandomSix() {
		String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
		return random;
	}

	/**
	 * 四位随机数
	 */
	public static String getRandomFour() {
		String random = (int) ((Math.random() * 9 + 1) * 1000) + "";
		return random;
	}

	/**
	 * 随机12位
	 */
	public static String getRandomTwelve() {
		return String.valueOf(new Date().getTime() - 1300000000000L);
	}

	/**
	 * 生成随机邀请码
	 */
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

	public static SqlSession openSession() {
		SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
		return sqlSessionFactory.openSession();
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	private static RedisUtils redisUtils = new RedisUtils();
	/*public static void main(String[] args){
	 *//*String dateStr1 = "2017-03-01 22:33:23";
		Date date1 = DateUtil.parse(dateStr1);

		String dateStr2 = "2017-03-01 21:33:22";
		Date date2 = DateUtil.parse(dateStr2);
		long str=DateUtil.between(date1,date2, DateUnit.MINUTE);
		System.out.println(str);
		String st="23:24:25";
		String[] s=st.split(":",-1);
		System.out.println(s[0]);
		System.out.println(RandomUtil.randomInt(99999999));
		System.out.println(getRandomTwelve());
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
		System.out.println(simpleDateFormat.format(new Date()));
		Integer ssd=Integer.valueOf(DateUtil.format(new Date(),"HH"));
		System.out.println(ssd);
		System.out.println(DateUtil.format(DateUtil.parse(dateStr2),"HH"));
		System.out.println("323");
		System.out.println(500*0.3);
		System.out.println("------------------------------------");
		*//**//**接亲时间*//**//*
		String marryTime="2020-01-14 17:04:23";
		*//**/
	/**
	 * 小时数
	 *//**//*
		String hour="3";
		System.out.println(DateUtil.format(DateUtil.offsetHour(DateUtil.parse(marryTime, "yyyy-MM-dd HH:mm:ss"), -Integer.valueOf(hour)),"yyyy-MM-dd HH:mm:ss"));*//*
		String dateStr1 = "2017-03-02 22:33:23";
		Date date1 = DateUtil.parse(dateStr1);

		String dateStr2 = "2017-03-02 21:33:23";
		Date date2 = DateUtil.parse(dateStr2);

		//相差一个月，31天
		long betweenDay = DateUtil.between(date2, date1, DateUnit.HOUR);
		System.out.println(betweenDay);
		String marraytime=DateUtil.format(DateUtil.parse(dateStr2),"yyyy-MM-dd");
		System.out.println(marraytime);
		String today=DateUtil.today();
		System.out.println(today);
		String s=DateUtil.format(DateUtil.offsetDay(DateUtil.parse(dateStr2), 1),"yyyy-MM-dd");
		System.out.println(s);
		String dateStr = "2020-01-31 23:32:23";
		Date date = DateUtil.parse(dateStr);

		//常用偏移，结果：2017-03-04 22:33:23
		DateTime newDate2 = DateUtil.offsetDay(date, 1);
		System.out.println(newDate2);
		List<String> list=new ArrayList<>();
		list.add("0");
		list.add("1");
		list.add("2");
		list.parallelStream().anyMatch((l)->{
			System.out.println(l);
			if("1".equals(l)){
				return true;
			}
			return false;
		});
		Double sd=250.05;
		System.out.println(sd/2);
		DecimalFormat decimalFormat = new DecimalFormat("0.##");

		double l = 250.10;

		String format = decimalFormat.format(l);

		System.out.println(format);

		String day=DateUtil.format(DateUtil.offsetDay(DateUtil.parse("2020-01-21 15:23:23"), 1),"yyyy-MM-dd");
		if(day.equals(DateUtil.today())){
			System.out.println("呵呵");
		}

	}*/

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
		Date date = new Date();
		return machineId + String.format("%015d", hashCodeV);
	}
	/*public static void main(String[] args){
		/*List<String> orderinfoList=new ArrayList<>();
		orderinfoList.add("1");
		orderinfoList.add("2");
		orderinfoList.add("3");
		System.out.println( Long.parseLong(String.valueOf(System.currentTimeMillis()/1000)));
		orderinfoList.parallelStream().forEach((orderInfo)->{
			System.out.println(orderInfo);
		});

		String s="12345";

		s=s.substring(0,s.indexOf("_"));
		System.out.println(s);*/
	/*Integer b = Double.valueOf(300 * 0.3).intValue();*/
	/*System.out.println(b);*/
		/*BigDecimal a =new BigDecimal("0.10");
		BigDecimal b =new BigDecimal("0.10");
		a=a.add(b);
		System.out.println(a);
		Map<String, Object> map=new HashMap<>();
		map.put("1","23");
		map.put("2",null);
		if(map.get("1")!=null&&map.get("2")!=null){
			System.out.println("yes");
		}*/





		/*List<Map<String,Object>> map=new ArrayList<>();
		Map<String,Object> map1=new HashMap<>();
		map1.put("name","重量");
		List<Map<String,Object>> m=new ArrayList<>();
		Map<String,Object> m_1=new HashMap<>();
		m_1.put("name","1斤");
		m_1.put("img","1斤图片");
		Map<String,Object> m_2=new HashMap<>();

		m_2.put("name","3斤");
		m_2.put("img","3斤图片");
		m.add(m_1);
		m.add(m_2);


		Map<String,Object> map2=new HashMap<>();
		map2.put("name","颜色");
		List<Map<String,Object>> m2=new ArrayList<>();
		Map<String,Object> m2_1=new HashMap<>();
		m2_1.put("name","红色");
		Map<String,Object> m2_2=new HashMap<>();

		m2_2.put("name","蓝色");
		m2.add(m2_1);
		m2.add(m2_2);
		map1.put("nameList",m);
		map2.put("nameList",m2);
		map.add(map1);
		map.add(map2);
		Map<String,Object> a=new HashMap<>();
		List<Map<String,Object>> listMap=new ArrayList<>();
		int i=0;
		for (Map<String,Object> result:map){
			String name=result.get("name").toString();
			System.out.println(name);
			List<Map<String,Object>> list=(List<Map<String,Object>>)result.get("nameList");

			List<String> b=new ArrayList<>();
			for (Map<String,Object> r:list){
				Map<String,Object> c=new HashMap<>();
				String name1=r.get("name").toString();
				b.add(name1);
				if(r.containsKey("img")){

					String img=r.get("img").toString();
					c.put(name,img);
					listMap.add(c);
					*//*System.out.println(img);*//*
				}

				System.out.println(name1);

			}
			a.put("name"+i,b);
			i++;
		}
		System.out.println(JSONUtils.toJson(a));*/
		/*List<String> list1=new ArrayList<>();
		list1.add("0");
		list1.add("1");
		List<String> list2=new ArrayList<>();
		list2.add("3");
		list2.add("4");
		list2=list1;
		System.out.println(list2);
		String str="324";*/






	// 测试 combineBeans方法
	public static void main(String[] args) {

		List<Map<String,Object>> list=new ArrayList<>();
		Map<String,Object> m=new HashMap<>();
		m.put("1","1");
		m.put("2","2");
		list.add(m);
		Map<String,Object> m2=new HashMap<>();
		m2.put("1","3");
		m2.put("4","4");
		list.add(m2);
		List<Map<String,Object>> list2=new ArrayList<>();
		for (Map<String,Object> m1:list){
			if(!m1.get("1").equals("1")){
				list2.add(m1);
				break;
			}
		}


		System.out.println(JSON.toJSONString(list2));
		String str="sdfdf_123";
		str=str.split("_")[1];
		System.out.println(str);



	}
	@Test
	public void test(){
		String data1="2020-04-19 11:12:10";
		String data2="2020-04-19 11:11:10";
		long betweenDay = DateUtil.between(DateUtil.parse(data1), DateUtil.parse(data2), DateUnit.MINUTE);
		System.out.println(betweenDay);
	}
	@Test
	public void sdfjds(){
		Chat chat=new Chat();
		chat.setChatid("123");
		chat.setIsRead("1");
		chat.setDelFlag("1");
		System.out.println(chat.getDelFlag());
	}




	
}
