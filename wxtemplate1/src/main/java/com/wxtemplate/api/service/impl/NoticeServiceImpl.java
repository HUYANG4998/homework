package com.wxtemplate.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wxtemplate.api.entity.Notice;
import com.wxtemplate.api.entity.User;
import com.wxtemplate.api.mapper.NoticeMapper;
import com.wxtemplate.api.mapper.UserMapper;
import com.wxtemplate.api.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxtemplate.api.util.PushtoSingle;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统通知 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Notice> selectNotice(String userid) {
        List<Notice> listNotice=new ArrayList<>();
        if(!StringUtils.isEmpty(userid)){

            listNotice=noticeMapper.selectNotice(userid);
        }
        return listNotice;
    }

    @Override
    public void deleteNotice(String noticeid) {
        if(!StringUtils.isEmpty(noticeid)){
            noticeMapper.deleteById(noticeid);
        }
    }

    @Override
    public Notice selectNoticeByNoticeId(String noticeid) {
        Notice notice=null;
        if(!StringUtils.isEmpty(noticeid)){
            notice=noticeMapper.selectById(noticeid);
        }
        return notice;
    }

    @Override
    public void addNotice(Notice notice) {
        if(notice!=null){
            notice.setAddtime(DateUtil.now());
            noticeMapper.insert(notice);
        }
    }

    @Override
    public void updateNotice(Notice notice) {
        if(notice!=null){
            noticeMapper.updateById(notice);
        }
    }

    @Override
    public Integer selectNoticeCount(String userid) {
        Integer count=0;
        if(!StringUtils.isEmpty(userid)){
            count=noticeMapper.selectNoticeCount(userid);
        }
        return count;
    }

    @Override
    public List<Notice> selectNoticeSys() {
        return noticeMapper.selectNoticeSys();
    }
}
