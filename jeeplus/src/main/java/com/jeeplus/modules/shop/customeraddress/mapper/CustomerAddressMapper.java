/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customeraddress.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.customeraddress.entity.CustomerAddress;

/**
 * 用户收货地址管理MAPPER接口
 * @author lhh
 * @version 2019-12-23
 */
@MyBatisMapper
public interface CustomerAddressMapper extends BaseMapper<CustomerAddress> {
	
}