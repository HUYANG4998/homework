package com.wxtemplate.wxtemplate.api.util;

import com.wxtemplate.wxtemplate.api.entity.ChatRecord;
import com.wxtemplate.wxtemplate.api.entity.Dynamic;

import java.math.BigDecimal;
import java.util.Random;

import static java.math.BigDecimal.ROUND_HALF_UP;

public class RedPackageUtils {

    public static BigDecimal getRandomMoney(Dynamic dynamic ){
        if(dynamic.getRedPackageNumber()==1){
            dynamic.setRedPackageNumber(dynamic.getRedPackageNumber()-1);
            BigDecimal bigDecimal = new BigDecimal((double) Math.round(dynamic.getRedPackageMoney().multiply(new BigDecimal(100)).doubleValue()) / 100).setScale(2, ROUND_HALF_UP);
            dynamic.setRedPackageMoney(BigDecimalUtils.subtract(dynamic.getRedPackageMoney(),bigDecimal));
            return bigDecimal;
        }
        if(dynamic.getRedPackageNumber() == 0){
            return new BigDecimal(0);
        }
        Random r   = new Random();
        double min   = 0.01;
        double max  = (BigDecimalUtils.divide(dynamic.getRedPackageMoney(),new BigDecimal(dynamic.getRedPackageNumber())).doubleValue()) * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01: money;
        money = Math.floor(money * 100) / 100;
        dynamic.setRedPackageNumber(dynamic.getRedPackageNumber()-1);
        dynamic.setRedPackageMoney(BigDecimalUtils.subtract(dynamic.getRedPackageMoney(),new BigDecimal(money)));
        BigDecimal bigDecimal = new BigDecimal(money);

        return bigDecimal.setScale(2, ROUND_HALF_UP);
    }
    public static BigDecimal getRandomMoney(ChatRecord chatRecord ){
        if(chatRecord.getRedNumber()==1){
            chatRecord.setRedNumber(chatRecord.getRedNumber()-1);
            BigDecimal bigDecimal = new BigDecimal((double) Math.round(chatRecord.getRedMoney().multiply(new BigDecimal(100)).doubleValue()) / 100).setScale(2, ROUND_HALF_UP);
            chatRecord.setRedMoney(BigDecimalUtils.subtract(chatRecord.getRedMoney(),bigDecimal));
            return bigDecimal;
        }
        if(chatRecord.getRedNumber() == 0){
            return new BigDecimal(0);
        }
        Random r   = new Random();
        double min   = 0.01;
        double max  = (BigDecimalUtils.divide(chatRecord.getRedMoney(),new BigDecimal(chatRecord.getRedNumber())).doubleValue()) * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01: money;
        money = Math.floor(money * 100) / 100;
        chatRecord.setRedNumber(chatRecord.getRedNumber()-1);
        chatRecord.setRedMoney(BigDecimalUtils.subtract(chatRecord.getRedMoney(),new BigDecimal(money)));
        BigDecimal bigDecimal = new BigDecimal(money);

        return bigDecimal.setScale(2, ROUND_HALF_UP);
    }
    public static void main(String[] args){
        Dynamic dynamic=new Dynamic();
        dynamic.setRedPackageMoney(new BigDecimal(100));
        dynamic.setRedPackageNumber(100);
     for (int i=0;i<100;i++){
         BigDecimal randomMoney = getRandomMoney(dynamic);
         System.out.println(randomMoney);
     }
    }
}
