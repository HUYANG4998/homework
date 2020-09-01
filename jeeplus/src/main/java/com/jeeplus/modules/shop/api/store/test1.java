package com.jeeplus.modules.shop.api.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Strings;
import com.jeeplus.modules.shop.util.JSONUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;

/**
 * @author lhh
 * @date 2019/12/14 0014
 */
public class test1 {
    public static void main(String[] args) {
       /* String json = "{\"firstname\":\"Bo\",\"lastname\":\"Shang\",\"age\":30}";
        String str = "[{\"code\":\"UUM70004\",\"message\":\"组织单元名称不能为空\",\"data\":{\"id\":\"254\",\"suborderNo\":\"SUB_2018062797348039\",\"organUnitType\":\"部门\",\"action\":\"add\",\"parent\":\"10000\",\"ordinal\":0,\"organUnitFullName\":\"组织单元全称\"},\"success\":false},{\"code\":\"UUM70004\",\"message\":\"组织单元名称不能为空\",\"data\":{\"id\":\"255\",\"suborderNo\":\"SUB_2018062797348039\",\"organUnitType\":\"部门\",\"action\":\"add\",\"parent\":\"10000\",\"ordinal\":0,\"organUnitFullName\":\"组织单元全称\"},\"success\":false}]";
        JSONArray jsonArray1 = (JSONArray) JSONObject.parse(str);
        System.err.println(jsonArray1);*/
        /*StringBuffer str = new StringBuffer("We Are Happy.");
        String str1 = str.toString();
        str1 = str1.replaceAll(" ","%20");
        System.out.println(str1);*/
        String s = "{color:['红色','黄色','白色'],neicun:['2G','4G','8G','16'],yuncun:['16','32','64']}";
        //s = "[color:[{name:'红色',img:'图片红色'},{name:'蓝色',img:'图片蓝色'}],bank:[{name:'蓝神'},{name:'红参'}]";




        Map<String,Object> ma12p=getJson();


        /*String[] sd=s.split(",");
        sdf=sdf.replace("[","");
        sdf=sdf.replace("]","");
        List<String> lsd=Arrays.asList(sdf.split(","));
        for (String l:lsd){
            System.out.println(l);
        }*/
      /*  String json="[\"color\":\"红色\",\"yuncun\":\"32\",\"neicun\":\"2G\"]";
        String js=JSON.toJSONString(listMap, SerializerFeature.WriteNonStringValueAsString);
        String strArr1 = "[{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}," +
                "{\"00\":\"zhangsan\",\"11\":\"lisi\",\"22\":\"wangwu\",\"33\":\"maliu\"}]";

        List<Map<String,String>> mapl122ist1=(List<Map<String,String>>) JSONArray.parse(js);
        for (Map<String,String> map12:mapl122ist1){
                *//*System.out.println(json);
                System.out.println(map12);*//*
                if(json.equals(map12.toString())){
                    System.out.println("1233");
                }

           *//* for (Map.Entry entry : map12.entrySet()){
                System.out.println( entry.getKey()  + "  " +entry.getValue());
            }*//*
        }
        if(mapl122ist1.contains(json)){
            System.out.println("yiyayiyawo");
        }
       System.out.println(JSON.toJSONString(listMap, SerializerFeature.WriteNonStringValueAsString));
*/

    }
    public static Map<String,Object> getJson(){
        List<Map<String,Object>> map=new ArrayList<>();
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
        int i=0;
        Map<String,Object> c=new HashMap<>();
        for (Map<String,Object> result:map){
            String name=result.get("name").toString();
            System.out.println(name);
            List<Map<String,Object>> list=(List<Map<String,Object>>)result.get("nameList");

            List<String> b=new ArrayList<>();

            for (Map<String,Object> r:list){

                String name1=r.get("name").toString();
                b.add(name1);
                if(r.containsKey("img")){

                    String img=r.get("img").toString();
                    c.put(name1,img);

                    /*System.out.println(img);*/
                }

                System.out.println(name1);

            }
            a.put("name"+i,b);
            i++;
        }
        Map<String,Object> sd=new HashMap<>();
        sd.put("name",JSONUtils.toJson(a));
        sd.put("img",c);
        return sd;

    }


    public static List<Map<String,Object>> getSpec(Map<String,Object> ma12p){
        String name=(String)ma12p.get("name");
        LinkedHashMap map = JSONObject.parseObject(name, LinkedHashMap.class, Feature.OrderedField);
        /*System.out.println(map);*/
        Iterator<Map.Entry<String, List<String>>> entries = map.entrySet().iterator();
        LinkedList<Object> objects1 = new LinkedList<>();
        while (entries.hasNext()) {
            Map.Entry<String, List<String>> entry = entries.next();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            /*System.out.println(key + ":" + value);*/
            ArrayList<Object> objects = new ArrayList<>();
            for (int i = 0; i < value.size(); i++) {
                String str = key + ":" + value.get(i);
                objects.add(str);
            }
            String[] array = (String[]) objects.toArray(new String[objects.size()]);

            objects1.add(array);
        }
        /*System.err.println(objects1);*/
        List<Map<String,Object>> listMap=new LinkedList<>();
        if(objects1.size()>1){
            List list=doExchange(objects1);
            String sdf=list.get(0).toString();
            sdf=sdf.substring(1);
            sdf=sdf.substring(0,sdf.length()-1);
            String[] strArr= sdf.split("],");

            for (int i=0;i<strArr.length;i++){
                strArr[i]=strArr[i].replace("[","");
                strArr[i]=strArr[i].replace("]","");
                String[] ss=strArr[i].split(",");
                Map<String,Object> map1=new LinkedHashMap<>();
                for(int j=0;j<ss.length;j++){
                    String[] sd=ss[j].split(":");

                    map1.put(sd[0].trim(),sd[1].trim());

                }
                listMap.add(map1);
            }
        }else{

            Map<String,Object> map1=JSONUtils.jsonToMap(name);
            for(Map.Entry<String, Object> entry : map1.entrySet()){
                List<String> value=(List<String>)entry.getValue();
                for (String str:value){
                    Map<String,Object> result=new LinkedHashMap<>();
                    result.put("name0",str);
                    listMap.add(result);
                }

            }

                /*String[] s=(String[])o;
                for (int i=0;i<s.length;i++){
                    Map<String,Object> result=new HashMap<>();
                    result.put("name0",s[i]);
                    listMap.add(result);
                }
                System.out.println(o);*/

        }

       /* if(ma12p.containsKey("img")){
            Map<String,Object> list23=(Map<String,Object>)ma12p.get("img");
            if(list23.size()>0){
                for (Map<String,Object> map1:listMap){
                    for(Map.Entry<String, Object> entry : map1.entrySet()){

                        if(list23.containsKey(entry.getValue())){
                            map1.put("img",list23.get(entry.getValue()));
                        }
                    }
                }
            }
        }*/
        return listMap;
    }
    private static List  doExchange(List arrayLists){
        List ss=new ArrayList<>();
        int len=arrayLists.size();
        //判断数组size是否小于2，如果小于说明已经递归完成了，否则你们懂得的，不懂？断续看代码
        if (len<2){
            //      this.arrayLists=arrayLists;
            return null;
        }
        //拿到第一个数组
        int len0;
        if (arrayLists.get(0) instanceof String[]){
            String[] arr0= (String[]) arrayLists.get(0);
            len0=arr0.length;
        }else {
            len0=((ArrayList)arrayLists.get(0)).size();
        }

        //拿到第二个数组
        String[] arr1= (String[]) arrayLists.get(1);
        int len1=arr1.length;

        //计算当前两个数组一共能够组成多少个组合
        int lenBoth=len0*len1;

        //定义临时存放排列数据的集合
        ArrayList<ArrayList<String>> tempArrayLists=new ArrayList<>(lenBoth);

        //第一层for就是循环arrayLists第一个元素的
        for (int i=0;i<len0;i++){
            //第二层for就是循环arrayLists第二个元素的
            for (int j=0;j<len1;j++){
                //判断第一个元素如果是数组说明，循环才刚开始
                if (arrayLists.get(0) instanceof String[]){
                    String[] arr0= (String[]) arrayLists.get(0);
                    ArrayList<String> arr=new ArrayList<>();
                    arr.add(arr0[i]);
                    arr.add(arr1[j]);
                    //把排列数据加到临时的集合中
                    tempArrayLists.add(arr);
                }else {
                    //到这里就明循环了最少一轮啦，我们把上一轮的结果拿出来继续跟arrayLists的下一个元素排列
                    ArrayList<ArrayList<String>> arrtemp= (ArrayList<ArrayList<String>>) arrayLists.get(0);
                    ArrayList<String> arr=new ArrayList<>();
                    for (int k=0;k<arrtemp.get(i).size();k++){
                        arr.add(arrtemp.get(i).get(k));
                    }
                    arr.add(arr1[j]);
                    tempArrayLists.add(arr);
                }
            }
        }

        //这是根据上面排列的结果重新生成的一个集合
        List newArrayLists=new ArrayList<>();

        //把还没排列的数组装进来，看清楚i=2的喔，因为前面两个数组已经完事了，不需要再加进来了
        for (int i=2;i<arrayLists.size();i++){
            newArrayLists.add(arrayLists.get(i));
        }
        //记得把我们辛苦排列的数据加到新集合的第一位喔，不然白忙了
        newArrayLists.add(0,tempArrayLists);

        //你没看错，我们这整个算法用到的就是递归的思想。
        ss= doExchange(newArrayLists);
        if(ss==null){return newArrayLists; }
        return ss;
    }

}

