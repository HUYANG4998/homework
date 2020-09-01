package com.wxtemplate.api.service.impl;

import com.wxtemplate.api.entity.Trip;
import com.wxtemplate.api.mapper.TripMapper;
import com.wxtemplate.api.service.ITripService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 行程 服务实现类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-18
 */
@Service
public class TripServiceImpl extends ServiceImpl<TripMapper, Trip> implements ITripService {

}
