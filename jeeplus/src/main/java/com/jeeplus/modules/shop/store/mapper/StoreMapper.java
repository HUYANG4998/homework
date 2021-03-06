/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.store.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.store.entity.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商家管理MAPPER接口
 * @author lhh
 * @version 2019-12-20
 */
@MyBatisMapper
public interface StoreMapper extends BaseMapper<Store> {

    Store likeUserId(@Param("userId") String userId);

    List<Store> getStoreAndWares(Store store);
}