package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.service.IUserReadService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户读公告表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
@RestController
@RequestMapping("/api/user-read")
public class UserReadController {

    @Resource
    private IUserReadService userReadService;

    /**
     * 查询我的公告
     *
     * @param request
     * @return
     */
    @PostMapping("/MyNotice")
    public Result MyNotice(HttpServletRequest request, Integer pageNo, Integer pageSize) {
        String userId = (String) request.getAttribute("userId");
        pageNo = pageNo == null ? 1 : pageNo;
        pageSize = pageSize == null ? 10 : pageSize;
        PageHelper.startPage(pageNo, pageSize, true, false, true);
        if (!StringUtils.isEmpty(userId)) {
            List<Map<String, Object>> listMap = userReadService.myNotice(userId);
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(listMap);
            return Result.success(pageInfo);
        } else {
            return Result.fail("参数异常");
        }
    }

    /**
     * 修改我的公告
     *
     * @param userReadId
     * @return
     */
    @PostMapping("/updateNotice")
    public Result MyNotice(String userReadId) {

        if (!StringUtils.isEmpty(userReadId)) {
            userReadService.updateNotice(userReadId);
            return Result.success("已读");
        } else {
            return Result.fail("参数异常");
        }
    }
}
