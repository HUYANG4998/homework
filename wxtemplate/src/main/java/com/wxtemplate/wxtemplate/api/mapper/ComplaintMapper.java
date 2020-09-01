package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Complaint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-08-10
 */
public interface ComplaintMapper extends BaseMapper<Complaint> {

    Integer selectByDynamicIdAndUserIdCount(@Param("dynamicId") String dynamicId, @Param("userId") String userId);

    List<Complaint> selectByDynamicId(@Param("dynamicId") String dynamicId);
}
