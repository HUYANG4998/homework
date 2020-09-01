package com.wxtemplate.wxtemplate.api.controller;


import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wxtemplate.wxtemplate.api.entity.Comment;
import com.wxtemplate.wxtemplate.api.service.ICommentService;
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
 * 评论表 前端控制器
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private ICommentService commentService;

    /**
     * 评论动态
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/insertCommentDynamic")
    public Result insertCommentDynamic(Comment comment, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        if(comment!=null&&!StringUtils.isEmpty(userId)){
            commentService.insertCommentDynamic(comment,userId);
            return Result.success("评论成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 评论回复
     * @param comment
     * @return
     */
    @PostMapping("/insetCommentReply")
    public Result insetCommentReply(Comment comment) {
        if(comment!=null){
            commentService.insetCommentReply(comment);
            return Result.success("评论成功");
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     *查询动态评论--动态id
     * @param dynamicId
     * @return
     */
    @PostMapping("/selectCommentByDynamicId")
    public Result selectCommentByDynamicId(String dynamicId,Integer pageNo,Integer pageSize) {
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?10:pageSize;
        if(!StringUtils.isEmpty(dynamicId)){
            PageHelper.startPage(pageNo, pageSize, true, false, true);
            List<Map<String,Object>> dynamicList=commentService.selectCommentByDynamicId(dynamicId);
            PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(dynamicList);
            return Result.success(pageInfo);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     *查询评论回复---根据评论id
     * @param commentId
     * @return
     */
    @PostMapping("/selectCommentByCommentId")
    public Result selectCommentByCommentId(String commentId) {
        if(!StringUtils.isEmpty(commentId)){
            return commentService.selectCommentByCommentId(commentId);
        }else{
            return Result.fail("参数异常");
        }
    }
    /**
     *查询评论---根据评论id
     * @param commentId
     * @return
     */
    @PostMapping("/selectByCommentId")
    public Result selectByCommentId(String commentId) {
        if(!StringUtils.isEmpty(commentId)){
            return commentService.selectByCommentId(commentId);
        }else{
            return Result.fail("参数异常");
        }
    }

    /**
     * 删除评论/评论回复--评论/评论回复id
     * @param commentId
     * @return
     */
    @PostMapping("/deleteCommentByCommentId")
    public Result deleteCommentByCommentId(String commentId) {
        if(!StringUtils.isEmpty(commentId)){
            commentService.deleteCommentByCommentId(commentId);
            return Result.success("删除成功");
        }else{
            return Result.fail("参数异常");
        }
    }
}
