package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Earn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收益表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-20
 */
public interface EarnMapper extends BaseMapper<Earn> {

    List<Earn> selectEarnByAddtime(String userid, String addtime);

    String yesterdayEarnAndprice(String userid, String day);
}
