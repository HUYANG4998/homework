package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Comment;
import com.wxtemplate.wxtemplate.api.mapper.CommentMapper;
import com.wxtemplate.wxtemplate.api.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.wxtemplate.tools.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public void insertCommentDynamic(Comment comment, String userId) {
        if(comment!=null&&!StringUtils.isEmpty(userId)){
            comment.setUserId(userId);
            comment.setAddtime(DateUtil.now());
            commentMapper.insert(comment);
        }
    }

    @Override
    public void insetCommentReply(Comment comment) {
        if(comment!=null){
            comment.setAddtime(DateUtil.now());
            commentMapper.insert(comment);
        }
    }

    @Override
    public List<Map<String, Object>> selectCommentByDynamicId(String dynamicId) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(dynamicId)){
            listMap=commentMapper.selectCommentByDynamicId(dynamicId);
            if(listMap.size()>0){
                for (Map<String,Object> map:listMap){
                    String commentId = (String)map.get("commentId");
                    map.put("nextComment",commentMapper.selectCommentByCommentId(commentId));
                }
            }


        }
        return listMap;
    }



    @Override
    public Result selectCommentByCommentId(String commentId) {
        List<Map<String,Object>> listMap=new ArrayList<>();
        if(!StringUtils.isEmpty(commentId)){
            listMap=commentMapper.selectCommentByCommentId(commentId);
        }
        return Result.success(listMap);
    }

    @Override
    public Result selectByCommentId(String commentId) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(commentId)){
            map=commentMapper.selectByCommentId(commentId);
        }
        return Result.success(map);
    }

    @Override
    public void deleteCommentByCommentId(String commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if(comment!=null){
            commentMapper.deleteById(comment.getCommentId());
        }
    }
}
