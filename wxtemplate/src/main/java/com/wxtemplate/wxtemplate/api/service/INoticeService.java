package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
public interface INoticeService extends IService<Notice> {

    void insert(Notice notice);

    List<Notice> selectNoticeAll();

    Notice selectNoticeById(String noticeId);

    void deleteNotice(String noticeId);

    void updateNotice(Notice notice);
}
