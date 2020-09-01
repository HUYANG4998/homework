package com.wxtemplate.wxtemplate.api.util;

import com.github.pagehelper.Page;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页方法封装
 * @author huy
 * @date 2019年4月11日
 */
public class PaginationUtil {

    /**
     * 生成分页页码-Header
     * @param page 分页对象
     * @return Map封装总计、总页数、当前页码
     */
    public static Map<String,Object> generatePaginationHttpHeaders(Page<?> page) {

        Map<String,Object> pageinfo = new HashMap<>();
        pageinfo.put("total-num", "" + page.getTotal());
        pageinfo.put("total-count", "" + page.getPages());    // 总页数
        pageinfo.put("current-page", "" + page.getPageNum());   // 当前页页码

        Map<String,Object> info = new HashMap<>();
        info.put("pageinfo",pageinfo);

        return info;
    }
    /**
     * 生成分页页码-JSON
     * @param page 分页对象
     * @param code 状态
     * @param msg 异常信息
     * @return Map封装状态、异常信息、分页信息
     */
    public static Map<String,Object> generatePaginationJSON(Page<?> page,int code,String msg) {

        Map<String,Object> pageinfo = new HashMap<>();
        pageinfo.put("total-num", "" + page.getTotal());
        pageinfo.put("total-count", "" + page.getPages());    // 总页数
        if ((page.getPages()==0)&&(page.getTotal()>0)){
            pageinfo.put("total-count", "" + 1);
        }
        pageinfo.put("current-page", "" + page.getPageNum());   // 当前页页码

        Map<String,Object> info = new HashMap<>();
        info.put("pageinfo",pageinfo);
        info.put("result",page);

        Map<String,Object> httpResult = new HashMap<>();
        httpResult.put("code",code);
        httpResult.put("msg",msg);
        httpResult.put("info",info);

        return httpResult;
    }

    /**
     * 生成分页页码
     * @param page 分页对象
     * @param code 状态
     * @param msg 异常信息
     * @param pages 总页数
     * @return Map封装：状态、异常信息、分页信息
     */
    public static Map<String,Object> generatePaginationJSONforManualPage(Page<?> page,int code,String msg,int pages) {

        Map<String,Object> pageinfo = new HashMap<>();
        pageinfo.put("total-num", "" + page.getTotal());
        pageinfo.put("total-count", "" + pages);    // 总页数
        pageinfo.put("current-page", "" + page.getPageNum());   // 当前页页码

        Map<String,Object> info = new HashMap<>();
        info.put("pageinfo",pageinfo);
        info.put("result",page);

        Map<String,Object> httpResult = new HashMap<>();
        httpResult.put("code",code);
        httpResult.put("msg",msg);
        httpResult.put("info",info);

        return httpResult;
    }
    /**
     * 生成正常JSON
     * @param result 对象
     * @param code 状态
     * @param msg 异常信息
     * @return Map封装：状态、异常信息、分页信息
     */
    public static Map<String,Object> generateNormalJSON(Object result,int code,String msg) {

        Map<String,Object> httpResult = new HashMap<>();
        httpResult.put("code",code);
        httpResult.put("msg",msg);
        if (result != null) {
            httpResult.put("info", result);
        }

        return httpResult;
    }

    /**
     * 生成需求-HttpHeader
     * @param totalCount 总页数
     * @param currentPage 当前页页码
     * @return HttpHeaders对象封装
     */
    public static HttpHeaders generateHotDemandHttpHeaders(String totalCount, String currentPage) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-total-count", "" + totalCount);    // 总页数
        headers.add("X-current-page", "" + currentPage);   // 当前页页码

        return headers;
    }

}
