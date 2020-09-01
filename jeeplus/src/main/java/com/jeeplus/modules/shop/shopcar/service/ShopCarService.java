/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.shopcar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import com.jeeplus.modules.shop.shopcar.mapper.ShopCarMapper;

/**
 * 购物车管理Service
 * @author lhh
 * @version 2019-12-27
 */
@Service
@Transactional(readOnly = true)
public class ShopCarService extends CrudService<ShopCarMapper, ShopCar> {

	public ShopCar get(String id) {
		return super.get(id);
	}
	
	public List<ShopCar> findList(ShopCar shopCar) {
		return super.findList(shopCar);
	}
	
	public Page<ShopCar> findPage(Page<ShopCar> page, ShopCar shopCar) {
		return super.findPage(page, shopCar);
	}
	
	@Transactional(readOnly = false)
	public void save(ShopCar shopCar) {
		super.save(shopCar);
	}
	
	@Transactional(readOnly = false)
	public void delete(ShopCar shopCar) {
		super.delete(shopCar);
	}
	
}