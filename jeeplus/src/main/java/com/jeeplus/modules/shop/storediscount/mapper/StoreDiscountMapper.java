/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storediscount.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;

import java.util.Map;
import java.util.List;

/**
 * 商家优惠MAPPER接口
 * @author lhh
 * @version 2019-11-29
 */
@MyBatisMapper
public interface StoreDiscountMapper extends BaseMapper<StoreDiscount> {
	List<Map<String,Object>> selectCurrentDiscounts();

    Integer selectDiscountsNumber();
}