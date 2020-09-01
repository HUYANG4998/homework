/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemovie.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.storemovie.entity.StoreMovie;

import java.util.List;

/**
 * 商家动态MAPPER接口
 * @author lhh
 * @version 2019-12-13
 */
@MyBatisMapper
public interface StoreMovieMapper extends BaseMapper<StoreMovie> {

    List<StoreMovie> findListFoccus(StoreMovie storeMovie);
}