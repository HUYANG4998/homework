package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动态分类表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface ICategoryService extends IService<Category> {

    List<Category> selectAllCategory(String name);

    void insertCategory(String name);

    void updateCategory(Category category);

    void deleteCategory(String categoryId);

    Category selectCategoryById(String categoryId);
}
