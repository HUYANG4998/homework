package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表
 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IDynamicService extends IService<Dynamic> {

    List<Map<String,Object>> selectDynamicByCategoryId(String categoryId,String userId);

    Result insertDynamic(Dynamic dynamic, String userId);

    Result updateDynamic(Dynamic dynamic, String userId);

    Result selectRedPackageDetails(String dynamicId, String userId);

    boolean judgeRedWars(String dynamicId, String userId);

    void deleteDynamic(String dynamicId);

    Map<String,Object> selectDynamicByDynamicId(String dynamicId,String userId);

    List<Dynamic> selectDynamicByUserId(String userId);

    Result dynamicRefresh(String dynamicId);

    void deleteAllDynamic();

    List<Map<String, Object>> selectRedPackageDynamic();

    List<Map<String,Object>> selectDynamicByContent(String content);

    List<Map<String,Object>> selectAllDynamic(String name);

    List<Map<String, Object>> selectRedMoneyDynamic();
}
