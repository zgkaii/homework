package com.zgkaii.concurreny.mission;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/08 23:43
 * @Description:
 **/
public class MyRunnable2 implements Runnable {
    @Override
    public void run() {
        Mark.mark();
        System.out.println("获取到线程： " + Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName() + "线程回到线程池");
    }
}
