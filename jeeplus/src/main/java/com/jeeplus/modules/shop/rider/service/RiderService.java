/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.rider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.rider.entity.Rider;
import com.jeeplus.modules.shop.rider.mapper.RiderMapper;

/**
 * 骑手管理Service
 * @author lhh
 * @version 2020-01-16
 */
@Service
@Transactional(readOnly = true)
public class RiderService extends CrudService<RiderMapper, Rider> {

	@Autowired
	private RiderMapper riderMapper;
	public Rider get(String id) {
		return super.get(id);
	}

	public List<Rider> findList(Rider rider) {
		return super.findList(rider);
	}
	public List<Rider> findLists(Rider rider) {
		return riderMapper.findList(rider);
	}

	public Page<Rider> findPage(Page<Rider> page, Rider rider) {
		return super.findPage(page, rider);
	}

	@Transactional(readOnly = false)
	public void save(Rider rider) {
		super.save(rider);
	}

	@Transactional(readOnly = false)
	public void delete(Rider rider) {
		super.delete(rider);
	}

}
