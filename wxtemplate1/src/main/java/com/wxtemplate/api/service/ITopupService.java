package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Topup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 手动充值记录 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-03-30
 */
public interface ITopupService extends IService<Topup> {

    List<Map<String, Object>> selectTopup(String phone);
}
