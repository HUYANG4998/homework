package com.wxtemplate.wxtemplate.api.util;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class Sort {
    public static List<Map<String, Object>> ListSort(List<Map<String, Object>> list) {
        {    //排序方法
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        // format.format(o1.getTime()) 表示 date转string类型 如果是string类型就不要转换了
                        Date dt1 = DateUtil.parse((String)o1.get("addtime"));
                        Date dt2 = DateUtil.parse((String)o2.get("addtime"));

                        // 这是由大向小排序   如果要由小向大转换比较符号就可以
                        if (dt1.getTime() < dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() > dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }

            });
            return list;
        }
    }
}
