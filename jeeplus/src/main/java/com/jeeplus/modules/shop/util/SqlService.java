package com.jeeplus.modules.shop.util;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.shop.api.earn.service.EarnService;
import com.jeeplus.modules.shop.order.service.OrderService;
import com.jeeplus.modules.shop.otherprice.entity.OtherPrice;
import com.jeeplus.modules.shop.otherprice.service.OtherPriceService;
import com.jeeplus.modules.shop.rider.service.RiderService;
import com.jeeplus.modules.shop.store.service.StoreService;
import com.jeeplus.modules.shop.storediscount.service.StoreDiscountService;
import com.jeeplus.modules.shop.systemtype.service.SystemTypeService;
import com.jeeplus.modules.shop.wares.service.WaresService;
import com.jeeplus.modules.shop.waresspecs.service.WaresSpecsService;

public class SqlService {
    public static StoreService storeService = SpringContextHolder.getBean(StoreService.class);
    public static SystemTypeService systemTypeService = SpringContextHolder.getBean(SystemTypeService.class);
    public static WaresService waresService = SpringContextHolder.getBean(WaresService.class);
    public static EarnService earnService = SpringContextHolder.getBean(EarnService.class);
    public static OrderService orderService = SpringContextHolder.getBean(OrderService.class);
    public static StoreDiscountService storeDiscountService= SpringContextHolder.getBean(StoreDiscountService.class);
    public static WaresSpecsService waresSpecsService = SpringContextHolder.getBean(WaresSpecsService.class);
    public static RiderService riderService=SpringContextHolder.getBean(RiderService.class);
    public static OtherPriceService otherPriceService=SpringContextHolder.getBean(OtherPriceService.class);
}
