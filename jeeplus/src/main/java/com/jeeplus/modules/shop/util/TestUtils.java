package com.jeeplus.modules.shop.util;

import com.jeeplus.modules.shop.storediscount.entity.StoreDiscount;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestUtils {
    private int value;

    public synchronized int   getNext() {
        return value++;
    }


    @Test
    public void  test1(){
        TestUtils unsafeSequence = new TestUtils();
        Executor executors = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 200; i++) {
            executors.execute(()-> System.out.println(unsafeSequence.getNext()));
        }
        Object o=new Object();

    }
    @Test
    public void test2(){
        Double money=1055.50;
        String format = new DecimalFormat("0.00").format(money);
        System.out.println(Double.valueOf(format));
    }
    @Test
    public void test3(){
       /* System.out.println(1.0 / 0.0);
        System.out.println(-1.0 / 0.0);
        System.out.println(0.0d / 0.0);*/
        String str="32";
        boolean contains = str.equals("32");
        List<String> list=new ArrayList<>();
        list.add("23");
        list.add("34");
        System.out.println(list.toString());

    }
    @Test
    public void test4(){
        BigDecimal b1=new BigDecimal("2");
        BigDecimal b2=new BigDecimal(2);

        int i = b1.compareTo(b2);
        System.out.println(i);
        String img="234";
        String[] split = img.split("[,]");
        System.out.println(split[0]);
    }
    @Test
    public void test5(){
        List<String> list=new ArrayList<>();
        list.add("111");
        list.add("222");
        for (String str:list){
            str="333";
        }
        System.out.println(list.toString());
    }

    public static void main(String[] args) {
        //创建锁对象,保证唯一
        Object obj = new Object();
        // 创建一个顾客线程(消费者)
        new Thread(){
            @Override
            public void run() {
                //一直等着买包子
                while(true){
                    //保证等待和唤醒的线程只能有一个执行,需要使用同步技术
                    synchronized (obj){
                        System.out.println("顾客1告知老板要的包子的种类和数量");
                        //调用wait方法,放弃cpu的执行,进入到WAITING状态(无限等待)
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //唤醒之后执行的代码
                        System.out.println("包子已经做好了,顾客1开吃!");
                        System.out.println("---------------------------------------");
                    }
                }
            }
        }.start();

        // 创建一个顾客线程(消费者)
        new Thread(){
            @Override
            public void run() {
                //一直等着买包子
                while(true){
                    //保证等待和唤醒的线程只能有一个执行,需要使用同步技术
                    synchronized (obj){
                        System.out.println("顾客2告知老板要的包子的种类和数量");
                        //调用wait方法,放弃cpu的执行,进入到WAITING状态(无限等待)
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //唤醒之后执行的代码
                        System.out.println("包子已经做好了,顾客2开吃!");
                        System.out.println("---------------------------------------");
                    }
                }
            }
        }.start();

        //创建一个老板线程(生产者)
        new Thread(){
            @Override
            public void run() {
                //一直做包子
                while (true){
                    //花了5秒做包子
                    try {
                        Thread.sleep(5000);//花5秒钟做包子
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //保证等待和唤醒的线程只能有一个执行,需要使用同步技术
                    synchronized (obj){
                        System.out.println("老板5秒钟之后做好包子,告知顾客,可以吃包子了");
                        //做好包子之后,调用notify方法,唤醒顾客吃包子
                        //obj.notify();//如果有多个等待线程,随机唤醒一个
                        obj.notify();//唤醒所有等待的线程
                    }
                }
            }
        }.start();
    }



}
