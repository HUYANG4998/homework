package com.wxtemplate.api.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class Bench1 {
    long v;

    public void func0() { v++; }
    public void func1() { v--; }
    public void func2() { v++; }
    public void func3() { v--; }

    public void testInterface() {
        long t = System.nanoTime();
        Runnable[] rs = {
                this::func0,
                this::func1,
                this::func2,
                this::func3,
        };

        for(int i = 0; i < 1_0000_0000; i++)
            rs[i & 3].run(); // 关键调用
        t = (System.nanoTime() - t) / 1_000_000;
        System.out.format("testInterface: %d %dms\n", v, t);
    }

    public void testReflect() throws Throwable {
        long t = System.nanoTime();
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle[] ms = {
                lookup.unreflect(Bench1.class.getMethod("func0")),
                lookup.unreflect(Bench1.class.getMethod("func1")),
                lookup.unreflect(Bench1.class.getMethod("func2")),
                lookup.unreflect(Bench1.class.getMethod("func3")),
        };
        for(int i = 0; i < 1_0000_0000; i++)
            ms[i & 3].invoke(this);
        t = (System.nanoTime() - t) / 1_000_000;
        System.out.format("testReflect  : %d %dms\n", v, t);
    }
    /*public void testReflect() throws Exception {
        long t = System.nanoTime();
        Method[] ms = {
                Bench1.class.getMethod("func0"),
                Bench1.class.getMethod("func1"),
                Bench1.class.getMethod("func2"),
                Bench1.class.getMethod("func3"),
        };
        for(int i = 0; i < 1_0000_0000; i++)
            ms[i & 3].invoke(this); // 关键调用
        t = (System.nanoTime() - t) / 1_000_000;
        System.out.format("testReflect  : %d %dms\n", v, t);
    }*/

    public static void main(String[] args) throws Throwable {
        Bench1 b;
        b = new Bench1(); // 预热部分
        b.testInterface();
        b = new Bench1();
        b.testReflect();

        b = new Bench1(); // 实测部分
        b.testInterface();
        b = new Bench1();
        b.testReflect();
    }
}
