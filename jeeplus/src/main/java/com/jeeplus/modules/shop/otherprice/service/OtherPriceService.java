/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.otherprice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.otherprice.mapper.OtherPriceMapper;

/**
 * 配送费设置Service
 * @author lhh
 * @version 2020-01-03
 */
@Service
@Transactional(readOnly = true)
public class OtherPriceService extends CrudService<OtherPriceMapper, OtherPrice> {

	public OtherPrice get(String id) {
		return super.get(id);
	}
	
	public List<OtherPrice> findList(OtherPrice otherPrice) {
		return super.findList(otherPrice);
	}
	
	public Page<OtherPrice> findPage(Page<OtherPrice> page, OtherPrice otherPrice) {
		return super.findPage(page, otherPrice);
	}
	
	@Transactional(readOnly = false)
	public void save(OtherPrice otherPrice) {
		super.save(otherPrice);
	}
	
	@Transactional(readOnly = false)
	public void delete(OtherPrice otherPrice) {
		super.delete(otherPrice);
	}
	
}