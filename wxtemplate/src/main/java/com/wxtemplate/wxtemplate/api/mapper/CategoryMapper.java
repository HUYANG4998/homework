package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 动态分类表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> selectAllCategory(@Param("name") String name);
}
