package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface ICommentService extends IService<Comment> {

    void insertCommentDynamic(Comment comment, String userId);

    void insetCommentReply(Comment comment);

    List<Map<String, Object>> selectCommentByDynamicId(String dynamicId);

    Result selectCommentByCommentId(String commentId);

    Result selectByCommentId(String commentId);

    void deleteCommentByCommentId(String commentId);
}
