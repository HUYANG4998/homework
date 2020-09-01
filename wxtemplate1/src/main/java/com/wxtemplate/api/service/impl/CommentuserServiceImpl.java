package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Commentuser;
import com.wxtemplate.api.mapper.CommentuserMapper;
import com.wxtemplate.api.service.ICommentuserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论用户 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
@Service
public class CommentuserServiceImpl extends ServiceImpl<CommentuserMapper, Commentuser> implements ICommentuserService {

    @Resource
    private CommentuserMapper commentuserMapper;

    @Override
    public Map<String, Object> selectCommentUserByOrderid(String orderid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(orderid)){
            map=commentuserMapper.selectCommentUserByOrderid(orderid);
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> selectCommentUser() {

        return  commentuserMapper.selectCommentUser();
    }

    @Override
    public Map<String, Object> selectCommentUserByCommentid(String commentid) {
        Map<String,Object> map=new HashMap<>();
        if(!StringUtils.isEmpty(commentid)){
            map=commentuserMapper.selectCommentUserByCommentid(commentid);
        }
        return map;
    }

    @Override
    public void updateCommentUserByCommentid(Commentuser commentuser) {
        if(commentuser!=null){
            commentuser.setUpdatetime(DateUtil.now());
            commentuserMapper.updateById(commentuser);
        }
    }
}
