package com.jeeplus.modules.shop.api.store;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lhh
 * @date 2019/12/12 0012
 */
public class Test {

    private String name;
    private  String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public static void main(String[] args){
        List<String> list=new ArrayList<>();
        list.add("胡杨");
        list.add("人才");
        if(list.contains("人")){
            System.out.println("1234");
        }
        String file = "http://localhost:8888/upload/20190310/115111_58_592_HDFS读取文件的流程.png";

        //截取文件名
        String oriName = file .substring(file .lastIndexOf("_")+1);
        System.out.println(oriName);

    }
}
