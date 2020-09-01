/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.shop.storemoviereply.service;

import java.util.List;

import com.jeeplus.modules.shop.storemovie.mapper.StoreMovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.shop.storemoviereply.entity.StoreMovieReply;
import com.jeeplus.modules.shop.storemoviereply.mapper.StoreMovieReplyMapper;

/**
 * 商家动态回复Service
 * @author lhh
 * @version 2020-01-07
 */
@Service
@Transactional(readOnly = true)
public class StoreMovieReplyService extends CrudService<StoreMovieReplyMapper, StoreMovieReply> {

	@Autowired
	StoreMovieReplyMapper storeMovieReplyMapper;
	public StoreMovieReply get(String id) {
		return super.get(id);
	}

	public List<StoreMovieReply> findList(StoreMovieReply storeMovieReply) {
		return super.findList(storeMovieReply);
	}

	public Page<StoreMovieReply> findPage(Page<StoreMovieReply> page, StoreMovieReply storeMovieReply) {
		return super.findPage(page, storeMovieReply);
	}
	//查询动态且lastId为null的评论
	public  List<StoreMovieReply> findAllByPropertyIdAndLastIdNull(StoreMovieReply storeMovieReply){
		return  storeMovieReplyMapper.findAllByPropertyIdAndLastIdNull(storeMovieReply);
	}
	//查询动态且lastId不为null的评论
	public  List<StoreMovieReply> findAllByPropertyIdAndLastIdNotNull(StoreMovieReply storeMovieReply){
		return  storeMovieReplyMapper.findAllByPropertyIdAndLastIdNotNull(storeMovieReply);
	}
	@Transactional(readOnly = false)
	public void save(StoreMovieReply storeMovieReply) {
		super.save(storeMovieReply);
	}

	@Transactional(readOnly = false)
	public void delete(StoreMovieReply storeMovieReply) {
		super.delete(storeMovieReply);
	}

}
