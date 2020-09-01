package com.wxtemplate.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.druid.util.StringUtils;
import com.wxtemplate.wxtemplate.api.entity.Notice;
import com.wxtemplate.wxtemplate.api.entity.User;
import com.wxtemplate.wxtemplate.api.entity.UserRead;
import com.wxtemplate.wxtemplate.api.mapper.NoticeMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.wxtemplate.api.mapper.UserReadMapper;
import com.wxtemplate.wxtemplate.api.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserReadMapper userReadMapper;
    @Override
    public void insert(Notice notice) {
        if(notice!=null){
            notice.setAddtime(DateUtil.now());
            noticeMapper.insert(notice);
            List<User> users = userMapper.selectUser();
            for(User user:users){
                UserRead userRead=new UserRead();
                userRead.setIsRead(0);
                userRead.setNoticeId(notice.getNoticeId());
                userRead.setUserId(user.getUserId());
                userReadMapper.insert(userRead);
            }
        }

    }

    @Override
    public List<Notice> selectNoticeAll() {

        return noticeMapper.selectNoticeAll();
    }

    @Override
    public Notice selectNoticeById(String noticeId) {
        Notice notice=null;
        if(!StringUtils.isEmpty(noticeId)){
            notice=noticeMapper.selectById(noticeId);
        }
        return notice;
    }

    @Override
    public void deleteNotice(String noticeId) {
        if(!StringUtils.isEmpty(noticeId)){
            noticeMapper.deleteById(noticeId);
        }
    }

    @Override
    public void updateNotice(Notice notice) {

        if(notice!=null){
            noticeMapper.updateById(notice);
        }
    }
}
