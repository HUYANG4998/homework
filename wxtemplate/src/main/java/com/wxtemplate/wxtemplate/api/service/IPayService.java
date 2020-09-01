package com.wxtemplate.wxtemplate.api.service;

import com.wxtemplate.wxtemplate.api.entity.Pay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wxtemplate.wxtemplate.tools.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2020-07-18
 */
public interface IPayService extends IService<Pay> {

    Result topUp(String userId, String money,String type);
}
