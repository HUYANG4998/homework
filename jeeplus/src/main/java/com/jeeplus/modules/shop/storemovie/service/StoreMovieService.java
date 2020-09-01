/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemovie.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;
import com.jeeplus.modules.shop.storemovie.mapper.StoreMovieMapper;

/**
 * 商家动态Service
 * @author lhh
 * @version 2019-12-13
 */
@Service
@Transactional(readOnly = true)
public class StoreMovieService extends CrudService<StoreMovieMapper, StoreMovie> {

	public StoreMovie get(String id) {
		return super.get(id);
	}
	
	public List<StoreMovie> findList(StoreMovie storeMovie) {
		return super.findList(storeMovie);
	}
	
	public Page<StoreMovie> findPage(Page<StoreMovie> page, StoreMovie storeMovie) {
		return super.findPage(page, storeMovie);
	}
	
	@Transactional(readOnly = false)
	public void save(StoreMovie storeMovie) {
		super.save(storeMovie);
	}
	
	@Transactional(readOnly = false)
	public void delete(StoreMovie storeMovie) {
		super.delete(storeMovie);
	}

    public Page<StoreMovie> findPageFocus(Page<StoreMovie> page, StoreMovie storeMovie) {
		dataRuleFilter(storeMovie);
		storeMovie.setPage(page);
		page.setList(mapper.findListFoccus(storeMovie));
		return page;
    }
}