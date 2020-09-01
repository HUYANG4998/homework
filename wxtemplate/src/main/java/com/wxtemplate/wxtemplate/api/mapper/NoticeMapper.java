package com.wxtemplate.wxtemplate.api.mapper;

import com.wxtemplate.wxtemplate.api.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-12
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    List<Notice> selectNoticeAll();
}
