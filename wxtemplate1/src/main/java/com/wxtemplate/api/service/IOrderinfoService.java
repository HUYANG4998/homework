package com.wxtemplate.api.service;

import com.wxtemplate.api.entity.Orderinfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单详情 服务类
 * </p>
 *
 * @author LIUWEI
 * @since 2019-12-16
 */
public interface IOrderinfoService extends IService<Orderinfo> {

    List<Map<String, Object>> selectOrderinfoByOrderid(String orderid,String type);

    Map<String, Object> selectOrderinfoByOrderinfoid(String orderinfoid);

    void updateOrderinfo(Orderinfo orderinfo);
}
