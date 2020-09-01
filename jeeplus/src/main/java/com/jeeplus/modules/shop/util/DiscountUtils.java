package com.jeeplus.modules.shop.util;

import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;

public class DiscountUtils {
    public static String getDiscount(StoreDiscount storeDiscount, Double price){
        String discountState = storeDiscount.getDiscountState();
        String manMoney = storeDiscount.getManMoney();
        if("1".equals(discountState)){
            //满减
            if(price>=Double.valueOf(manMoney)){
                price=Double.valueOf(storeDiscount.getMsg());
            }
        }else if("3".equals(discountState)){
            //折扣

            price=price*(Double.valueOf(storeDiscount.getMsg())/10);
        }
        return String.valueOf(price);
    }
}
