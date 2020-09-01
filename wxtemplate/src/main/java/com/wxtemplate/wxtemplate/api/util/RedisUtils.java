
package com.wxtemplate.wxtemplate.api.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类.</br>
 * (基于RedisTemplate)
 * @author xcbeyond
 * 2018年7月19日下午2:56:24
 */
@Component
public class RedisUtils {
 
	@Autowired
	private RedisTemplate<String,String> redisTemplate;


	/*
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key){
	    return key==null?null:redisTemplate.opsForValue().get(key);  
	}
	
	/**
	53
	     * 判断key是否存在
	54
	     * @param key 键
	55
	     * @return true 存在 false不存在
	56*/

	    public boolean hasKey(String key) {
	        try {
	            return redisTemplate.hasKey(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	  /*
	 * 写入缓存
	 */
	public boolean set(final String key, String value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean set(String key, String value, long time) {

		try {

			if (time > 0) {

				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);

			} else {

				set(key, value);

			}

			return true;

		} catch (Exception e) {

			e.printStackTrace();

			return false;

		}

	}
	/*
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败*/

	/*public boolean set(String key, String value, long time) {
	    try {
	        if (time > 0) {
	            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	        } else {
	            set(key, value);
	        }
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}*/
 
	/**
	 * 更新缓存
	 *//*
	public boolean getAndSet(final String key, String value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().getAndSet(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
 
	/**
	 * 删除缓存
	 */
	public boolean delete(final String key) {
		boolean result = false;
		try {
			redisTemplate.delete(key);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**

	public static void main(String[] args) {

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("1","23");
		map.put("2","45");
		RedisUtils redisUtils=new RedisUtils();
		redisUtils.set("23","234");
		*//*redisUtils.add("map",map);*//*
		*//*String dateStr1 = "2017-03-01 22:33:23";
		Date date1 = DateUtil.parse(dateStr1);

		String dateStr2 = "2017-04-01 23:33:23";
		Date date2 = DateUtil.parse(dateStr2);

		//相差一个月，31天
		long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
		System.out.println(betweenDay);
		
		
		String dateStr = "2017-03-01 22:33:23";
		Date date = DateUtil.parse(dateStr);

		//结果：2017-03-03 22:33:23
		Date newDate = DateUtil.offset(date, DateField.DAY_OF_MONTH, 31);
		
		String format = DateUtil.format(newDate, "yyyy-MM-dd");
		System.out.println(format);*//*
	*//*	String format = DateUtil.format(new Date(), "yyyyMMdd");
		System.out.println(format);*//*
		
	}*/
}
