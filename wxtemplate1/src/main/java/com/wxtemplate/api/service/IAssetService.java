package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Asset;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产表 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-06
 */
public interface IAssetService extends IService<Asset> {


    List<Map<String, Object>> selectAssetToAudit(String cate);

    List<Map<String, Object>> selectAssetRecord(String cate);

    Map<String, Object> selectAssetByAssetId(String assetid);

    void topUpAudit(Map<String, Object> result);

    void withdrawalAudit(Map<String, Object> result);
}
