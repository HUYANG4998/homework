package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Realname;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实名 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface IRealnameService extends IService<Realname> {

    List<Map<String, Object>> selectRealname(String realname);

    Map<String, Object> selectRealnameByRealnameId(String realnameid);

    void idcardAudit(Map<String, Object> result);
}
