package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.FriendsInvite;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.service.IFriendsInviteService;
import com.wxtemplate.wxtemplate.api.service.IUserService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友邀请 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-21
 */
@RestController
@RequestMapping("/api/friends-invite")
@CrossOrigin
public class FriendsInviteController {

    @Autowired
    private IFriendsInviteService friendsInviteService;
    @Autowired
    private IUserService userService;

    /**
     * 邀请好友
     * @param map
     * @return
     */
    @PostMapping("/insertFriends")
    public Result insertFriends(@RequestBody Map<String,List<FriendsInvite>> map, HttpServletRequest request) {
       List<FriendsInvite> friendsInviteList =  map.get("friendsInviteList")==null?null:map.get("friendsInviteList");
        String userId = (String) request.getAttribute("userId");
        if(friendsInviteList!=null&&friendsInviteList.size()>0&&!StringUtils.isEmpty(userId)){
            boolean isSuccess=friendsInviteService.insertFriends(friendsInviteList);
            return Result.success(isSuccess);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 查询邀请我的验证信息
     */
    @PostMapping("/selectFriendsInvite")
    public Result selectFriendsInvite(HttpServletRequest request,Integer pageNo,Integer pageSize) {
        String userId = (String) request.getAttribute("userId");
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        if(!StringUtils.isEmpty(userId)){
            List<Map<String,Object>> friendsInviteList=friendsInviteService.selectFriendsInvite(userId,pageNo,pageSize);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(friendsInviteList);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 邀请审核
     */
    @PostMapping("/inviteAudit")
    public Result inviteAudit(FriendsInvite friendsInvite) {

        if(friendsInvite!=null){
            friendsInviteService.inviteAudit(friendsInvite);
            return Result.success("审核完成");
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 删除邀请
     */
    @PostMapping("/deleteInvite")
    public Result deleteInvite(String friendsInviteId) {

        if(!StringUtils.isEmpty(friendsInviteId)){
            friendsInviteService.deleteInvite(friendsInviteId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查询用户和群聊---根据ID
     */
    @PostMapping("/selectUserAndGroupChat")
    public Result inviteAudit(String ID,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(ID)&&!StringUtils.isEmpty(userId)){
            User user = userService.getById(userId);
            if(user!=null){
                if(user.getSerNumber().equals(ID)){
                    return Result.fail("无需查询自己");
                }
            }
            Map<String,Object> map=friendsInviteService.selectUserAndGroupChat(ID,userId);
            return Result.success(map);
        }else{
            return Result.fail("参数异常");
        }
    }
}
