package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Webpic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 网站三个大图 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-23
 */
public interface WebpicMapper extends BaseMapper<Webpic> {

    Webpic selectWebPic();
}
