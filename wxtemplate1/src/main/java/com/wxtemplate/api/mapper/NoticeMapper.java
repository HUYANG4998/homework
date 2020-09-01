package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统通知 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-08
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    List<Notice> selectNotice(String userid);

    Integer selectNoticeCount(String userid);

    List<Notice> selectNoticeSys();
}
