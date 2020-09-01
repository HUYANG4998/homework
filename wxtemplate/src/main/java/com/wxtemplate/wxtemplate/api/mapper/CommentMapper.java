package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface CommentMapper extends BaseMapper<Comment> {

    Integer selectCommentPeopleByDynamicId(@Param("dynamicId") String dynamicId);

    List<Map<String, Object>> selectCommentByDynamicId(@Param("dynamicId") String dynamicId);

    List<Map<String, Object>> selectCommentByCommentId(@Param("commentId") String commentId);

    Map<String, Object> selectByCommentId(@Param("commentId") String commentId);

    List<Map<String, Object>> selectCommentByListDynamicId(List<String> list);
}
