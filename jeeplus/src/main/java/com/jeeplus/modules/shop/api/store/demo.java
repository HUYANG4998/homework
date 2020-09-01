package com.jeeplus.modules.shop.api.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;

/**
 * @author lhh
 * @date 2019/12/12 0012
 */

        import java.util.Map.Entry;

public class demo {
    public static void main(String[] args) {
        String s = "{'内存':['2G'],'颜色':['红色','玫瑰金'],'合约':['签合约','非合约'],'版本':['内地','港版']}";
        JSONObject jsonObject = JSON.parseObject(s);
        List<Param> params = new ArrayList<>();
        for (String currentKey : jsonObject.keySet()) {
            params.add(new Param(currentKey, (List<String>) jsonObject.get(currentKey)));
        }
        if (params.size() <= 1) {
            System.out.println("无需转换");
            return;
        }

        List<Map<String, String>> array = new ArrayList<>();
        Map<String, String> object;
        Param genParam = params.get(0);
        Map<String, List<String>> repectMap = new HashMap<>();

        for (int j = 0; j < genParam.getValues().size(); j++) {
            String value = genParam.getValues().get(j);
            object = new LinkedHashMap<>();
            object.put(genParam.getName(), value);
            for (int k = 1; k < params.size(); k++) {
                Param param = params.get(k);
                for (int l = 0; l < param.getValues().size(); l++) {
                    value = param.getValues().get(l);
                    if (object.containsKey(param.getName())) {
                        if (repectMap.containsKey(param.getName())) {
                            repectMap.get(param.getName()).add(value);
                        } else {
                            repectMap.put(param.getName(), Lists.newArrayList(value));
                        }
                    } else {
                        object.put(param.getName(), value);
                    }
                }
            }
            array.add(object);
        }

        Map<String, String> cloneMap;
        Set<String> set = new HashSet<>();
        StringBuilder key;
        for (Map.Entry<String, List<String>> entry : repectMap.entrySet()) {
            for (String repectValue : entry.getValue()) {
                for (int i = 0, length = array.size(); i < length; i++) {
                    Map<String, String> next = array.get(i);
                    cloneMap = new LinkedHashMap<>(next);
                    cloneMap.put(entry.getKey(), repectValue);

                    key = new StringBuilder();
                    for (Map.Entry<String, String> entry2 : cloneMap.entrySet()) {
                        key.append("-").append(entry2.getKey()).append("-").append(entry2.getValue());
                    }
                    if (!set.contains(key.toString())) {
                        array.add(cloneMap);
                    }
                    set.add(key.toString());
                }
            }
        }

        JSONArray result = JSON.parseArray(JSON.toJSONString(array));
        System.out.println(result);
        System.out.println(result.size());

        // String d = "  [{'内存':'4G','颜色':'红色'},{'内存':'2G','颜色':'红色'}]";
    }

    public static class Param {
        private String name;
        private List<String> values;
        public Param(String name, List<String> values) {
            this.name = name;
            this.values = values;
        }
        public String getName() {
            return name;
        }
        public List<String> getValues() {
            return values;
        }
    }

}
