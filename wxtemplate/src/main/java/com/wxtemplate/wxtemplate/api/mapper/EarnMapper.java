package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Earn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收益记录 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface EarnMapper extends BaseMapper<Earn> {

    Map<String,Object> selectEarnByDynamicIdAndUserId(@Param("dynamicId") String dynamicId, @Param("userId") String userId);

    List<Map<String, Object>> selectEarnByDynamicIdAndRemoveUserId(@Param("dynamicId") String dynamicId, @Param("userId") String userId);

    List<Map<String, Object>> selectBillRecords(@Param("userId") String userId);
}
