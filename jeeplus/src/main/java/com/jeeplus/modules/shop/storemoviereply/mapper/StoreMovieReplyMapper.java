/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemoviereply.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.shop.storemoviereply.entity.StoreMovieReply;

import java.util.List;

/**
 * 商家动态回复MAPPER接口
 * @author lhh
 * @version 2020-01-07
 */
@MyBatisMapper
public interface StoreMovieReplyMapper extends BaseMapper<StoreMovieReply> {

    public List<StoreMovieReply> findAllByPropertyIdAndLastIdNull(StoreMovieReply storeMovieReply);
    public List<StoreMovieReply> findAllByPropertyIdAndLastIdNotNull(StoreMovieReply storeMovieReply);
}
