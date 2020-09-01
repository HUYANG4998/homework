/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.customer.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.customer.entity.Customer;

/**
 * 用户管理MAPPER接口
 * @author lhh
 * @version 2019-11-21
 */
@MyBatisMapper
public interface CustomerMapper extends BaseMapper<Customer> {
	
}