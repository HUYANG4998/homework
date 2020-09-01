package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Category;
import com.wxtemplate.wxtemplate.api.mapper.CategoryMapper;
import com.wxtemplate.wxtemplate.api.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 动态分类表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> selectAllCategory(String name) {
        return categoryMapper.selectAllCategory(name);

    }

    @Override
    public void insertCategory(String name) {
        if(!StringUtils.isEmpty(name)){
            Category category=new Category();
            category.setAddtime(DateUtil.now());
            category.setUpdatetime(DateUtil.now());
            category.setName(name);
            categoryMapper.insert(category);
        }
    }

    @Override
    public void updateCategory(Category category) {
        if(category!=null){
            categoryMapper.updateById(category);
        }
    }

    @Override
    public void deleteCategory(String categoryId) {
        if(!StringUtils.isEmpty(categoryId)){
            categoryMapper.deleteById(categoryId);
        }
    }

    @Override
    public Category selectCategoryById(String categoryId) {
        Category category=null;
        if(!StringUtils.isEmpty(categoryId)){
            category=categoryMapper.selectById(categoryId);
        }
        return category;
    }


}
