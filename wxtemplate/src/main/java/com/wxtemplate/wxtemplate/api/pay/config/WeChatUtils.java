package com.wxtemplate.wxtemplate.api.pay.config;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class WeChatUtils {


    /**
     * 解析微信服务器发来的请求
     *
     * @param inputStream
     * @return 微信返回的参数集合
     */
    public static SortedMap<String, String> parseXmlString(InputStream inputStream) {
        SortedMap<String, String> map = new TreeMap<>();
        try {
            //获取request输入流
            SAXReader reader = new SAXReader();

            Document document = reader.read(inputStream);
            //得到xml根元素
            Element root = document.getRootElement();
            //得到根元素所有节点
            List<Element> elementList = root.elements();
            //遍历所有子节点
            for (Element element : elementList) {
                map.put(element.getName(), element.getText().toString());
            }
            //释放资源
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 解析微信服务器发来的请求
     *
     * @param inputStream
     * @return 微信返回的参数集合
     */
    public static SortedMap<String, Object> parseXml(InputStream inputStream) {
        SortedMap<String, Object> map = new TreeMap<>();
        try {
            //获取request输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            //得到xml根元素
            Element root = document.getRootElement();
            //得到根元素所有节点
            List<Element> elementList = root.elements();
            //遍历所有子节点
            for (Element element : elementList) {
                map.put(element.getName(), element.getText().toString());
            }
            //释放资源
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
