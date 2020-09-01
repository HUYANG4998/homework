package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.FriendCircle;
import com.wxtemplate.wxtemplate.api.service.IFriendCircleService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  朋友圈
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/friend-circle")
public class FriendCircleController {

    @Autowired
    private IFriendCircleService friendCircleService;

    /**
     * 删除朋友圈
     * @param friendCircleId
     * @return
     */
    @PostMapping("/deleteFriendCircle")
    public Result deleteFriendCircle(String friendCircleId) {
        if(!StringUtils.isEmpty(friendCircleId)){
            friendCircleService.deleteFriendCircle(friendCircleId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 添加朋友圈
     * @param friendCircle
     * @return
     */
    @PostMapping("/insertFriendCircle")
    public Result insertFriendCircle(FriendCircle friendCircle) {
        if(friendCircle!=null){
            friendCircleService.insertFriendCircle(friendCircle);
            return Result.success("添加成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询朋友圈   中间广告暂不做
     * @param request
     * @return
     */
    @PostMapping("/selectFriendCircle")
    public Result selectFriendCircle(HttpServletRequest request,Integer pageNo,Integer pageSize) {
        String userId=(String) request.getAttribute("userId");
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        if(!StringUtils.isEmpty(userId)){
            List<Map<String,Object>> list=friendCircleService.selectFriendCircle(userId,pageNo,pageSize);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(list);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询朋友圈   根据friendCircleId
     * @param friendCircleId
     * @param request
     * @return
     */
    @PostMapping("/selectFriendCircleById")
    public Result selectFriendCircleById(String friendCircleId,HttpServletRequest request) {
        String userId=(String) request.getAttribute("userId");

        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(friendCircleId)){
            Map<String,Object> friendCircle=friendCircleService.selectFriendCircleById(friendCircleId,userId);
            return Result.success(friendCircle);
        }else{
            return Result.fail("参数异常");
        }
    }
}
