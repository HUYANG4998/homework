/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storediscount.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import com.jeeplus.modules.shop.storediscount.mapper.StoreDiscountMapper;

/**
 * 商家优惠Service
 * @author lhh
 * @version 2019-11-29
 */
@Service
@Transactional(readOnly = true)
public class StoreDiscountService extends CrudService<StoreDiscountMapper, StoreDiscount> {

	@Autowired
	private StoreDiscountMapper storeDiscountMapper;
	public StoreDiscount get(String id) {
		return super.get(id);
	}
	
	public List<StoreDiscount> findList(StoreDiscount storeDiscount) {
		return storeDiscountMapper.findList(storeDiscount);
	}
	public List<Map<String,Object>> selectCurrentDiscounts(){
		return storeDiscountMapper.selectCurrentDiscounts();
	}
	
	public Page<StoreDiscount> findPage(Page<StoreDiscount> page, StoreDiscount storeDiscount) {
		return super.findPage(page, storeDiscount);
	}
	
	@Transactional(readOnly = false)
	public void save(StoreDiscount storeDiscount) {
		super.save(storeDiscount);
	}
	
	@Transactional(readOnly = false)
	public void delete(StoreDiscount storeDiscount) {
		super.delete(storeDiscount);
	}

    public Integer selectDiscountsNumber() {
		return storeDiscountMapper.selectDiscountsNumber();
    }
}