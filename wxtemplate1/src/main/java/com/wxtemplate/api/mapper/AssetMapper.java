package com.wxtemplate.api.mapper;

import com.wxtemplate.api.entity.Asset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资产表 Mapper 接口
 * </p>
 *
 * @author LIUWEI
 * @since 2020-01-06
 */
public interface AssetMapper extends BaseMapper<Asset> {

    Asset selectWechat(String type);

    Asset selectAlipay(String type);

    List<Map<String, Object>> selectAssetToAudit(String cate);

    List<Map<String, Object>> selectAssetRecord(String cate);

    Map<String, Object> selectAssetByAssetId(String assetid);

    Asset selectAssetByUseridAndDitch(String userid, String ditch);

    List<Asset> selectAssetList();
}
