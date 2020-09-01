package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.service.IEarnService;
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
 * 收益记录 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/earn")
@CrossOrigin
public class EarnController {

    @Autowired
    private IEarnService earnService;

    /**
     * 动态抢红包
     */
    @PostMapping("/sendRedPackage")
    public Result sendRedPackage(String dynamicId, HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");
        if (!StringUtils.isEmpty(dynamicId) && !StringUtils.isEmpty(userId)) {
            return earnService.sendRedPackage(dynamicId, userId);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询账单记录
     */
    @PostMapping("/selectBillRecords")
    public Result selectBillRecords(String userId, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        if (!StringUtils.isEmpty(userId)) {
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            List<Map<String, Object>> mapList = earnService.selectBillRecords(userId);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(mapList);
            return Result.success(pageInfo);
        } else {
            return Result.fail("参数异常");
        }
    }
}
