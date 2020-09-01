package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统通知 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
public interface INoticeService extends IService<Notice> {

    List<Notice> selectNotice(String userid);

    void deleteNotice(String noticeid);

    Notice selectNoticeByNoticeId(String noticeid);

    void addNotice(Notice notice);

    void updateNotice(Notice notice);

    Integer selectNoticeCount(String userid);

    List<Notice> selectNoticeSys();
}
