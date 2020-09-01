package com.wxtemplate.wxtemplate.api.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.GroupChat;
import com.wxtemplate.wxtemplate.api.service.IGroupChatService;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群聊 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/group-chat")
@CrossOrigin
public class GroupChatController {

    @Autowired
    private IGroupChatService groupChatService;

    /**
     * 创建群聊
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/insertGroupChat")
    public Result insertGroupChat(@RequestBody Map<String,Object> map, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");

        if(map!=null&&!map.isEmpty()&&!StringUtils.isEmpty(userId)){
            groupChatService.insertGroupChat(map,userId);
            return Result.success("创建成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 解散群聊
     * @param groupChatId
     * @return
     */
    @PostMapping("/deleteGroupChat")
    public Result deleteGroupChat(String groupChatId) {
        if(!StringUtils.isEmpty(groupChatId)){
            groupChatService.deleteGroupChat(groupChatId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 查看群聊信息
     * @param groupChatId
     * @return
     */
    @PostMapping("/selectGroupChatById")
    public Result selectGroupChatById(String groupChatId,HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(groupChatId)&&!StringUtils.isEmpty(userId)){
            Map<String,Object> groupChatMap=groupChatService.selectGroupChatById(groupChatId,userId);
            return Result.success(groupChatMap);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 查看全部群成员信息
     * @param groupChatId
     * @return
     */
    @PostMapping("/selectGroupMember")
    public Result selectGroupMember(String groupChatId) {
        if(!StringUtils.isEmpty(groupChatId)){
            List<Map<String,Object>> listMap=groupChatService.selectGroupMember(groupChatId);
            return Result.success(listMap);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 查询所有普通群成员--群聊id
     * @param groupChatId
     * @return
     */
    @PostMapping("/selectGroupMemberStaff")
    public Result selectGroupMemberStaff(String groupChatId) {
        if(!StringUtils.isEmpty(groupChatId)){
            List<Map<String,Object>> listMap=groupChatService.selectGroupMemberStaff(groupChatId);
            return Result.success(listMap);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 修改群聊信息
     */
    @PostMapping("/updateGroupChat")
    public Result updateGroupChat(GroupChat groupChat) {
        if(groupChat!=null){
            groupChatService.updateGroupChat(groupChat);
            return Result.success("修改成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     *  查询群聊 ---我加入的/我创建的
     */
    @PostMapping("/selectGroupChatCreateOrJoin")
    public Result selectGroupChatCreateOrJoin(String type,HttpServletRequest request,Integer pageNo,Integer pageSize) {
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        String userId = (String) request.getAttribute("userId");
        if(!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(type)){
            List<GroupChat> groupChatList=groupChatService.selectGroupChatCreateOrJoin(userId,type,pageNo,pageSize);
            PageInfo<GroupChat> pageInfo = new PageInfo<>(groupChatList);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 群聊设置管理员
     * @param groupMemberIdList
     * @return
     */
    @PostMapping("/setAdmin")
    public Result setAdmin(List<String> groupMemberIdList) {

        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            groupChatService.setAdmin(groupMemberIdList);
            return Result.success("设置成功");
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 群聊设置禁言
     * @param groupMemberIdList
     * @return
     */
    @PostMapping("/setBanned")
    public Result setBanned(List<String> groupMemberIdList) {

        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            groupChatService.setBanned(groupMemberIdList);
            return Result.success("设置成功");
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     * 踢出群聊
     * @param map
     * @return
     */
    @PostMapping("/exitGroupChat")
    public Result exitGroupChat(@RequestBody Map<String,Object> map) {
        List<String> groupMemberIdList=map.get("groupMemberIdList")==null?null:(List<String>)map.get("groupMemberIdList");

        if(groupMemberIdList!=null&&groupMemberIdList.size()>0){
            groupChatService.exitGroupChat(groupMemberIdList);
            return Result.success("设置成功");
        }else{
            return Result.fail("参数异常");
        }
    }

}
