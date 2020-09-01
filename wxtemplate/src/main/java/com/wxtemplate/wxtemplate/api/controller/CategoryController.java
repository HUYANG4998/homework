package com.wxtemplate.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Category;
import com.wxtemplate.wxtemplate.api.service.ICategoryService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 动态分类表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    /**
     * 查询全部动态分类
     *
     * @return
     */
    @PostMapping("/selectAllCategory")
    public Result selectAllCategory(String name) {

        return Result.success(categoryService.selectAllCategory(name));
    }

    /**
     * 添加动态分类
     *
     * @return
     */
    @PostMapping("/insertCategory")
    public Result insertCategory(String name) {
        if (!StringUtils.isEmpty(name)) {
            categoryService.insertCategory(name);
            return Result.success("添加成功");
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 修改动态分类
     *
     * @return
     */
    @PostMapping("/updateCategory")
    public Result updateCategory(Category category) {
        if (category != null) {
            categoryService.updateCategory(category);
            return Result.success("修改成功");
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除动态分类
     *
     * @return
     */
    @PostMapping("/deleteCategory")
    public Result deleteCategory(String categoryId) {
        if (!StringUtils.isEmpty(categoryId)) {
            categoryService.deleteCategory(categoryId);
            return Result.success("删除成功");
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询动态分类
     *
     * @return
     */
    @PostMapping("/selectCategoryById")
    public Result selectCategoryById(String categoryId) {
        if (!StringUtils.isEmpty(categoryId)) {
            Category category = categoryService.selectCategoryById(categoryId);
            if (category != null) {
                return Result.success(category);
            } else {
                return Result.fail("动态分类查询失败");
            }
        } else {
            return Result.fail("参数异常");
        }
    }
}
