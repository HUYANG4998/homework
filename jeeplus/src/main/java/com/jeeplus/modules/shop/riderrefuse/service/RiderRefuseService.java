/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.riderrefuse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.riderrefuse.entity.RiderRefuse;
import com.jeeplus.modules.shop.riderrefuse.mapper.RiderRefuseMapper;

/**
 * 骑手拒绝订单记录Service
 * @author lhh
 * @version 2020-01-17
 */
@Service
@Transactional(readOnly = true)
public class RiderRefuseService extends CrudService<RiderRefuseMapper, RiderRefuse> {

	@Autowired
	private RiderRefuseMapper riderRefuseMapper;
	public RiderRefuse get(String id) {
		return super.get(id);
	}

	public List<RiderRefuse> findList(RiderRefuse riderRefuse) {
		return super.findList(riderRefuse);
	}


	public Page<RiderRefuse> findPage(Page<RiderRefuse> page, RiderRefuse riderRefuse) {
		return super.findPage(page, riderRefuse);
	}

	@Transactional(readOnly = false)
	public void save(RiderRefuse riderRefuse) {
		super.save(riderRefuse);
	}

	@Transactional(readOnly = false)
	public void delete(RiderRefuse riderRefuse) {
		super.delete(riderRefuse);
	}

	public int findTodayCount(String rider_id) {
		return riderRefuseMapper.findTodayList(rider_id);
	}
}
