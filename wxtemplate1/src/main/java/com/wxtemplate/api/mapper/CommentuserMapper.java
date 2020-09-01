package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Commentuser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论用户 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface CommentuserMapper extends BaseMapper<Commentuser> {

    List<Map<String, Object>> selectMyEval(String userid);

    Map<String, Object> selectCommentUserByOrderid(String orderid);

    List<Map<String, Object>> selectCommentUser();

    Map<String, Object> selectCommentUserByCommentid(String commentid);

    List<Map<String, Object>> selectofficialcarComment();
}
