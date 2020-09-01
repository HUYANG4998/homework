package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;
import com.wxtemplate.wxtemplate.api.service.IDynamicService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表
 * 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/dynamic")
@CrossOrigin
public class DynamicController {

    @Autowired
    private IDynamicService dynamicService;

    /**
     * 查询动态--动态分类id
     *
     * @return
     */
    @PostMapping("/selectDynamicByCategoryId")
    public Result selectDynamicByCategoryId(String categoryId, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        pageSize = pageSize == null ? 10 : pageSize;
        pageNo = pageNo == null ? 1 : pageNo;
        if (!StringUtils.isEmpty(categoryId) && !StringUtils.isEmpty(userId)) {
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            List<Map<String, Object>> dynamicList = dynamicService.selectDynamicByCategoryId(categoryId, userId);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(dynamicList);
            return Result.success(pageInfo);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 添加动态
     *
     * @return
     */
    @PostMapping("/insertDynamic")
    public Result insertDynamic(Dynamic dynamic, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (dynamic != null && !StringUtils.isEmpty(userId)) {
            return dynamicService.insertDynamic(dynamic, userId);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除动态信息
     *
     * @return
     */
    @PostMapping("/deleteDynamic")
    public Result deleteDynamic(String dynamicId) {
        if (!StringUtils.isEmpty(dynamicId)) {
            dynamicService.deleteDynamic(dynamicId);
            return Result.success("删除成功");
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 修改动态信息
     *
     * @return
     */
    @PostMapping("/updateDynamic")
    public Result updateDynamic(Dynamic dynamic, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (dynamic != null && !StringUtils.isEmpty(userId)) {
            return dynamicService.updateDynamic(dynamic, userId);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询动态--动态id
     *
     * @return
     */
    @PostMapping("/selectDynamicByDynamicId")
    public Result selectDynamicByDynamicId(String dynamicId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(dynamicId) && !StringUtils.isEmpty(userId)) {
            Map<String, Object> map = dynamicService.selectDynamicByDynamicId(dynamicId, userId);
            if (map != null && !map.isEmpty()) {
                return Result.success(map);
            } else {
                return Result.fail("动态不存在");
            }
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询动态--用户id
     *
     * @param
     * @return
     */
    @PostMapping("/selectDynamicByUserId")
    public Result selectDynamicByUserId(String userId, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        if (!StringUtils.isEmpty(userId)) {
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            return Result.success(new PageInfo<>(dynamicService.selectDynamicByUserId(userId)));

        } else {
            return Result.fail("参数异常");
        }
    }
    /**
     * 查询动态模糊查询
     *
     * @param
     * @return
     */
    @PostMapping("/selectDynamicByContent")
    public Result selectDynamicByContent(String content, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        if (!StringUtils.isEmpty(content)) {
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            return Result.success(new PageInfo<>(dynamicService.selectDynamicByContent(content)));

        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 刷新动态
     *
     * @param dynamicId
     * @return
     */
    @PostMapping("/dynamicRefresh")
    public Result dynamicRefresh(String dynamicId) {
        if (!StringUtils.isEmpty(dynamicId)) {
            return dynamicService.dynamicRefresh(dynamicId);

        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除所有动态
     *
     * @return
     */
    @PostMapping("/deleteAllDynamic")
    public Result deleteAllDynamic() {

        dynamicService.deleteAllDynamic();

        return Result.success("删除成功");
    }

    /**
     * 查询带有红包的动态
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @PostMapping("/selectRedPackageDynamic")
    public Result selectRedPackageDynamic(Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(pageNo, pageSize, true, false, true);
        List<Map<String, Object>> listMap = dynamicService.selectRedPackageDynamic();
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(listMap);
        return Result.success(pageInfo);
    }


    /**
     * 查看红包详情
     *
     * @return
     */
    @PostMapping("/selectRedPackageDetails")
    public Result selectRedPackageDetails(String dynamicId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(dynamicId) && !StringUtils.isEmpty(userId)) {
            return dynamicService.selectRedPackageDetails(dynamicId, userId);

        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 抢红包---判断是否已抢
     *
     * @return
     */
    @PostMapping("/judgeRedWars")
    public Result judgeRedWars(String dynamicId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if (!StringUtils.isEmpty(dynamicId) && !StringUtils.isEmpty(userId)) {
            return Result.success(dynamicService.judgeRedWars(dynamicId, userId));
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询全部动态
     *
     * @return
     */
    @PostMapping("/selectAllDynamic")
    public Result selectAllDynamic(String name) {

            return Result.success(dynamicService.selectAllDynamic(name));

    }


}
