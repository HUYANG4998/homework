package com.wxtemplate.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json字符串工具类
 * 
 * @author K0230015
 * @date 2019年4月11日
 */
public final class JSONUtils {

	/**
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @param format 时间格式字符串
	 * @return String 日期格式字符串
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * json格式字符串转对象
	 * 
	 * @param json
	 *            字符串
	 * @return JSONArray json数组
	 */
	public static JSONArray stringToJsonArray(String json) {
		return JSONArray.parseArray(json);
	}

	/**
	 * json格式字符串转对象
	 * 
	 * @param json 字符串
	 *            
	 * @return JSONObject 对象
	 */
	public static JSONObject stringToJsonObject(String json) {
		return JSONObject.parseObject(json);
	}
	
	/**
	 * json格式字符串转对象集合
	 * @param json json字符串
	 * @param clazz 类型
	 * @param <T> 泛型
	 * @return 对象集合
	 */
	public static <T> List<T> toBeanArray(String json, Class<T> clazz) {

		if (StringUtils.isEmpty(json)) {
			return null;
		}

		return JSON.parseArray(json, clazz);
	}

	/**
	 * json字符串转换为相应对象
	 * 
	 * @param json 字符串
	 *            
	 * @param clazz 类型
	 *            
	 * @param <T> 泛型
	 *            
	 * @return 结果
	 */
	public static <T> T toBean(String json, Class<T> clazz) {

		if (StringUtils.isEmpty(json)) {
			return null;
		}

		return JSON.parseObject(json, clazz);
	}

	/**
	 * 对象之间转换，例如map类型转换为其他VO类型
	 * 
	 * @param object 对象
	 *            
	 * @param clazz 类型
	 *            
	 * @param <T> 泛型
	 *            
	 * @return 结果
	 */
	public static <T> T toBean(Object object, Class<T> clazz) {

		if (null == object) {
			return null;
		}

		return JSON.parseObject(toJson(object), clazz);
	}

	/**
	 * 把json串直接转化为list数组
	 * 
	 * @param json json字符串
	 *            
	 * @param clazz 类型
	 *            
	 * @param <T> 泛型
	 *            
	 * @return 数组
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {

		if (StringUtils.isEmpty(json)) {
			return null;
		}

		return JSON.parseArray(json, clazz);
	}

	/**
	 * json转化为Map对象
	 * 
	 * @param jsonStr json字符串
	 * @return Map 对象
	 */
	public static Map<String, Object> jsonToMap(String jsonStr) {
		JSONObject jsonObj = JSONObject.parseObject(jsonStr);
		Set<String> set = jsonObj.keySet();
		Map<String, Object> outMap = new HashMap<String, Object>();
		for (String key : set) {
			outMap.put(key, jsonObj.get(key));
		}
		return outMap;

	}

	
	/**
	 * 生成对象
	 * @return String 字符串
	 */
	public static String toJSONForJsonParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("curentPage", 1);
		map.put("params", new HashMap<String, Object>());
		return toJson(map);
	}

	/**
	 * 生成uuid字符串
	 * 
	 * @return String 字符串
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;
	}

	
	/**
	 * 把普通对象转换为json字符串
	 * @param object 普通对象
	 * @return String 字符串
	 */
	public static String toJson(Object object) {

		if (null == object) {
			return null;
		}

		return JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue);
	}

	/**
	 * 判断字符是否是数字
	 * 
	 * @param str 字符串
	 * @return true/false
	 */
	public static boolean isNumeric(String str) {
		
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	* 判断字符串是否可以转化为json对象
	* @param content 字符串
	* @return true/false
	*/
	public static boolean isJsonObject(String content) {
	    if(StringUtils.isEmpty(content)){
	        return false;
	    }
	    try {
	        JSONObject.parseObject(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/**
	* 判断字符串是否可以转化为JSON数组
	* @param content 字符串
	* @return true/false
	*/
	public static boolean isJsonArray(String content) {
	    if(StringUtils.isEmpty(content)){
	        return false;
	    }
	    try {
	        JSONArray.parseArray(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}