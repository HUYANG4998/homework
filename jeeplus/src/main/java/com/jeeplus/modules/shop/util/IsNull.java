package com.jeeplus.modules.shop.util;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class IsNull {
    private static int objectNullSystemOutFlag = 0;
    public static boolean objCheckIsNull(Object object, List<String> excludeNameList, List<Object> excludeValueList) {
        if (object == null) {
            return true;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 得到所有属性
        Field[] fields = clazz.getDeclaredFields();
        //判断入参
        boolean excludeNameListFlag = false;
        if (excludeNameList != null && excludeNameList.size() > 0) {
            excludeNameListFlag = true;
        }
        boolean excludeValueListFlag = false;
        if (excludeValueList != null && excludeValueList.size() > 0) {
            excludeValueListFlag = true;
        }
        //定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            String fieldName = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
                //得到属性类型
                Type fieldType = field.getGenericType();
                //得到属性名
                fieldName = field.getName();
                //剔除指定属性名的属性值
                if (excludeNameListFlag) {
                    for (String s : excludeNameList) {
                        if (fieldName.equals(s)) {
                            fieldValue = null;
                        }
                    }
                }
                //剔除指定属性值
                if (excludeValueListFlag) {
                    for (Object obj : excludeValueList) {
                        if (fieldValue == obj) {
                            fieldValue = null;
                        }
                    }
                }
                //打印输出(调试用可忽略)
                if (objectNullSystemOutFlag == 1) {
                    System.out.println("属性类型：" + fieldType + ",属性名：" + fieldName + ",属性值：" + fieldValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println(fieldValue);
            //只要有一个属性值不为null 就返回false 表示对象不为null 忽略序列化
            if (!StringUtils.isEmpty(fieldValue) && !"serialVersionUID".equals(fieldName)) {
                flag = false;
                break;
            }
        }
        //打印输出(调试用可忽略)
        if (objectNullSystemOutFlag == 1) {
            System.out.println("忽略属性: " + excludeNameList + " 忽略值: " + excludeValueList);
        }
        return flag;
    }
    public static boolean objCheckIsNull(Object object) {
        if (object == null) {
            return true;
        }
        // 得到类对象
        Class clazz = object.getClass();
        // 得到所有属性
        Field[] fields = clazz.getDeclaredFields();
        //定义返回结果，默认为true
        boolean flag = true;
        for (Field field : fields) {
            //设置权限（很重要，否则获取不到private的属性，不了解的同学补习一下反射知识）
            field.setAccessible(true);
            Object fieldValue = null;
            String fieldName = null;
            try {
                //得到属性值
                fieldValue = field.get(object);
                //得到属性类型
                Type fieldType = field.getGenericType();
                //得到属性名
                fieldName = field.getName();
                //打印输出(调试用可忽略)
                if (objectNullSystemOutFlag == 1) {
                    System.out.println("属性类型：" + fieldType + ",属性名：" + fieldName + ",属性值：" + fieldValue);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            //只要有一个属性值不为null 就返回false 表示对象不为null
            if (fieldValue != null) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
