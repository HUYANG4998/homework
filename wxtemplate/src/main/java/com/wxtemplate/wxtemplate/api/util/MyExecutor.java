package com.wxtemplate.wxtemplate.api.util;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyExecutor {
   static ExecutorService executor = Executors.newCachedThreadPool();

    public static void test1(){
        executor.submit(()->{
            System.out.println("正在执行业务，请稍等");
            try {
                Thread.sleep(5000);
                //TODO 执行具体的业务逻辑
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("业务执行完成");
        });
    }
    public static void main(String[] args){
          /* test1();
           System.out.println("异步处理完成");*/

    }

}
