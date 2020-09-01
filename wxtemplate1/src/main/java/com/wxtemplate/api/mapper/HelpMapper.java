package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Help;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 帮助中心 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-18
 */
public interface HelpMapper extends BaseMapper<Help> {

    List<Help> selectHelpStyle();

    List<Map<String,Object>> selectViceHelpById();

    List<Map<String,Object>> selectProHelpById();

    List<String> selectByFirstId(String firstId);

    List<Help> selectByFirstIdAll(String firstId);
}
