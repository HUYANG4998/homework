/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.express.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.express.entity.Express;
import com.jeeplus.modules.shop.express.mapper.ExpressMapper;

/**
 * 快递方式配置Service
 * @author lhh
 * @version 2020-01-15
 */
@Service
@Transactional(readOnly = true)
public class ExpressService extends CrudService<ExpressMapper, Express> {

	public Express get(String id) {
		return super.get(id);
	}
	
	public List<Express> findList(Express express) {
		return super.findList(express);
	}
	
	public Page<Express> findPage(Page<Express> page, Express express) {
		return super.findPage(page, express);
	}
	
	@Transactional(readOnly = false)
	public void save(Express express) {
		super.save(express);
	}
	
	@Transactional(readOnly = false)
	public void delete(Express express) {
		super.delete(express);
	}
	
}