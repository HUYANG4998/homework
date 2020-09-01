package com.wxtemplate.tools;

import java.util.HashMap;
import java.util.Map;

public class MyStaticMap {
    public static Map<Object,Object> map = new HashMap<>();

    public static void set(Object key,Object val){
        map.put(key,val);
    }
    public static boolean hasKey(Object key){
        return map.containsKey(key);
    }
    public static String get(Object key){
        return map.get(key).toString();
    }

}
