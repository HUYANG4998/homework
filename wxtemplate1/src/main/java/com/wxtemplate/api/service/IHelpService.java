package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Help;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.api.entity.SonHelp;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 帮助中心 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-04-18
 */
public interface IHelpService extends IService<Help> {

    List<Help> selectHelpStyle();

    void deletHelp(String helpId);

    Help selectHelpById(String helpId);

    void addStyle(Help help);

    void updateHelp(Help help);

    List<Map<String,Object>> selectViceHelp();

    List<Map<String,Object>> selectProHelp();

    List<Map<String, Object>> selectStyleAllAndOne();

    List<Help> selectViceProblems(String firstId);
}
