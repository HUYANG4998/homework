package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Advertising;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

import java.util.List;

/**
 * <p>
 * 广告表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IAdvertisingService extends IService<Advertising> {

    Result<String> insertAdvertising(Advertising advertising);

    void deleteAdvertising(List<String> advertisingIdList);

    void updateAdvertising(Advertising advertising);

    Advertising selectAdvertisingById(String advertisingId);

    List<Advertising> selectAllAdvertising(String type);

    List<Advertising> selectAdvertisingFive(String type);

    List<Advertising> selectAdvertingByUserId(String userId);
}
