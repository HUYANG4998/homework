package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.entity.DynamicCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态点赞/收藏 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface DynamicCollectionMapper extends BaseMapper<DynamicCollection> {

    List<Map<String, Object>> selectDynamicCollectionByUserId(String userId);

    void deleteByDynamicId(String dynamicId);

    List<Map<String, Object>> selectDynamicCollectionByMyDynamic(List<Dynamic> dynamicList);

    Integer selectDynamicCollectionByUserIdAndDynamicIdAndType(Map<String, Object> result);
}
