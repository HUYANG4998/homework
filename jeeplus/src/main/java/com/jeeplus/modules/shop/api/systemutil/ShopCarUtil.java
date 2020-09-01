package com.jeeplus.modules.shop.api.systemutil;

import com.jeeplus.modules.shop.order.entity.OrderDetail;
import com.jeeplus.modules.shop.shopcar.entity.ShopCar;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lhh
 * @date 2019/12/27 0027
 */
@Getter
@Setter
public class ShopCarUtil {

    private   String img;
    private String name;
    private List list;
    private  String storeId;
    private Double total=0.0;
}
