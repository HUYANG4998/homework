package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.DynamicCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 动态点赞/收藏 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-05-18
 */
public interface IDynamicCollectionService extends IService<DynamicCollection> {

    void insertDynamicCollection(DynamicCollection dynamicCollection);

    void deleteDynamicCollection(DynamicCollection dynamicCollection);
}
