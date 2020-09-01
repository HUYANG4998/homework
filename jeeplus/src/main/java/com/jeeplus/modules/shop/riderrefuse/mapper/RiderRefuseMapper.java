/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.riderrefuse.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.riderrefuse.entity.RiderRefuse;

/**
 * 骑手拒绝订单记录MAPPER接口
 * @author lhh
 * @version 2020-01-17
 */
@MyBatisMapper
public interface RiderRefuseMapper extends BaseMapper<RiderRefuse> {

    public int findTodayList(String rider_id);
}
