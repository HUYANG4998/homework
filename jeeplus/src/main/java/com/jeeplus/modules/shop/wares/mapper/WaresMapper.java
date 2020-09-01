/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.wares.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.wares.entity.Wares;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品管理MAPPER接口
 * @author lhh
 * @version 2019-12-18
 */
@MyBatisMapper
public interface WaresMapper extends BaseMapper<Wares> {
	List<Wares> findWaresByStore(Wares wares);

    List<Wares> getWaresByStoreIdAndName(@Param("id") String id, @Param("name") String name);
}
