package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表
 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface DynamicMapper extends BaseMapper<Dynamic> {

    List<Map<String,Object>> selectDynamicByCategoryId(@Param("categoryId") String categoryId);

    List<Dynamic> selectDynamicByUserId(@Param("userId") String userId);

    List<Dynamic> selectDynamic();

    void deleteAllDynamic();

    List<Map<String, Object>> selectRedPackageDynamic();

    List<Map<String,Object>>  selectDynamicByFriends(@Param("friends") List<String> friends);

    List<Map<String, Object>> selectDynamicByContent(@Param("content") String content);

    List<Map<String, Object>> selectAllDynamic(String name);

    Integer selectDynamicCount(String stick);

    List<Map<String, Object>> selectRedMoneyDynamic();
}
