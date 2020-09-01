/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.waresspecs.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.waresspecs.entity.WaresSpecs;

import java.util.Map;

/**
 * 商品规格MAPPER接口
 * @author lhh
 * @version 2019-12-02
 */
@MyBatisMapper
public interface WaresSpecsMapper extends BaseMapper<WaresSpecs> {

    void deleteByWaresId(Map<String, Object> m);
}