package com.wxtemplate.wxtemplate.api.util;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class BigDecimalUtils {

    /**加法*/
    public static BigDecimal add(BigDecimal a,BigDecimal b){
        return setScale(a.add(b));
    }
    /**减法*/
    public static BigDecimal subtract(BigDecimal a,BigDecimal b){
        return setScale(a.subtract(b));
    }
    /**乘法*/
    public static BigDecimal multiply(BigDecimal a,BigDecimal b){
        return setScale(a.multiply(b));
    }
    /**除法*/
    public static BigDecimal divide(BigDecimal a,BigDecimal b){
        return a.divide(b,2, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal setScale(BigDecimal a){
        return a.setScale(2,ROUND_HALF_UP);
    }
}
