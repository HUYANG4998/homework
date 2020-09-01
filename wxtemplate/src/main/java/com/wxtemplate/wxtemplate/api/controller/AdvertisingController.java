package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Advertising;
import com.wxtemplate.wxtemplate.api.service.IAdvertisingService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * <p>
 * 广告表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/advertising")
@CrossOrigin
public class AdvertisingController {

    @Autowired
    private IAdvertisingService advertisingService;

    /**
     * 发布广告
     *
     * @param advertising
     * @return
     */
    @PostMapping("/insertAdvertising")
    public Result<String> insertAdvertising(Advertising advertising) {
        if (advertising != null) {
            return advertisingService.insertAdvertising(advertising);
        } else {
            return Result.fail("发布失败");
        }
    }

    /**
     * 删除广告
     *
     * @param advertisingIdList
     * @return
     */
    @PostMapping("/deleteAdvertising")
    public Result<String> deleteAdvertising(String[] advertisingIdList) {
        if (advertisingIdList != null && Arrays.asList(advertisingIdList).size() > 0) {
            advertisingService.deleteAdvertising(Arrays.asList(advertisingIdList));
            return Result.success("删除成功");
        } else {
            return Result.fail("删除失败");
        }

    }

    /**
     * 修改广告
     *
     * @param advertising
     * @return
     */
    @PostMapping("/updateAdvertising")
    public Result<String> updateAdvertising(Advertising advertising) {
        if (advertising != null) {
            advertisingService.updateAdvertising(advertising);
            return Result.success("修改成功");
        } else {
            return Result.fail("修改失败");
        }
    }

    /**
     * 查询广告内容--根据广告id
     *
     * @param advertisingId
     * @return
     */
    @PostMapping("/selectAdvertisingById")
    public Result<Advertising> selectAdvertisingById(String advertisingId) {
        if (!StringUtils.isEmpty(advertisingId)) {
            return Result.success(advertisingService.selectAdvertisingById(advertisingId));
        } else {
            return Result.fail("查询失败");
        }
    }

    /**
     * 查询广告--用户id
     *
     * @param request
     * @return
     */
    @PostMapping("/selectAdvertingByUserId")
    public Result<Object> selectAdvertingByUserId(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(userId)) {
            return Result.success(advertisingService.selectAdvertingByUserId(userId));
        } else {
            return Result.fail("查询失败");
        }
    }


    /**
     * 查询全部广告
     *
     * @return
     */
    @PostMapping("/selectAllAdvertising")
    public Result<Object> selectAllAdvertising(String type) {
        return Result.success(advertisingService.selectAllAdvertising(type));
    }

    /**
     * 匹配私人广告--5张
     *
     * @param type
     * @return
     */
    @PostMapping("/selectAdvertisingFive")
    public Result<Object> selectAdvertisingFive(String type) {
        if (!StringUtils.isEmpty(type)) {
            return Result.success(advertisingService.selectAdvertisingFive(type));
        } else {
            return Result.fail("查询失败");
        }
    }
}
