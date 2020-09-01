package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Topup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 手动充值记录 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-30
 */
public interface TopupMapper extends BaseMapper<Topup> {

    List<Map<String, Object>> selectTopUp(@Param("phone") String phone);
}
