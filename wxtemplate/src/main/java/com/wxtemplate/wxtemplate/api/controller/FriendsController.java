package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.service.IFriendsService;
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
 * 好友 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@RestController
@RequestMapping("/api/friends")
@CrossOrigin
public class FriendsController {

    @Autowired
    private IFriendsService friendsService;
    /**
     * 查询我的好友
     */
    @PostMapping("/selectFriends")
    public Result selectFriends(HttpServletRequest request,Integer pageNo,Integer pageSize,String nickName) {
        String userId = (String) request.getAttribute("userId");
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        if(!StringUtils.isEmpty(userId)){
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            List<Map<String,Object>> listMap=friendsService.selectFriends(userId,nickName);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(listMap);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除好友
     * @param friendsId
     * @return
     */
    @PostMapping("/deleteFriendsById")
    public Result deleteFriendsById(String friendsId) {
        if(!StringUtils.isEmpty(friendsId)){
            friendsService.deleteFriendsById(friendsId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }
}
