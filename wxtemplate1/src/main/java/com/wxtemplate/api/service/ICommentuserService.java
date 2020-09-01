package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Commentuser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论用户 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface ICommentuserService extends IService<Commentuser> {

    Map<String, Object> selectCommentUserByOrderid(String orderid);

    List<Map<String, Object>> selectCommentUser();

    Map<String, Object> selectCommentUserByCommentid(String commentid);

    void updateCommentUserByCommentid(Commentuser commentuser);
}
