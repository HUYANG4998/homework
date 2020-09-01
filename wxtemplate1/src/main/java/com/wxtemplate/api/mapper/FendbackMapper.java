package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Fendback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 反馈表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface FendbackMapper extends BaseMapper<Fendback> {

    List<Fendback> selectFeedback();
}
