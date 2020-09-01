package com.jeeplus.modules.shop.api.earn.mapper;


import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.api.earn.entity.Earn;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@MyBatisMapper
public interface EarnMapper extends BaseMapper<Earn> {
    List<Earn> selectEarnByAddtime(@Param("userId") String userId, @Param("addtime") String addtime);
    String yesterdayEarnAndprice(@Param("userId") String userId,@Param("addtime") String addtime);
    void addEarn(Earn earn);

}
