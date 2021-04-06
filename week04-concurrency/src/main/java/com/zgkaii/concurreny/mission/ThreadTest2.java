package com.zgkaii.concurreny.mission;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/06 11:25
 * @Description:
 */
public class ThreadTest2 {
    public static void main(String[] args) {
        System.out.println("主线程开始执行");
        MyRunnable mr = new MyRunnable();
        Thread t = new Thread(mr, "子线程");
        // 启动新线程
        t.start();

        System.out.println("主线程开始执行");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Mark.mark();
                System.out.println(Thread.currentThread().getName() + "正在执行");
            }
        }).start();
    }

    public static class MyRunnable implements Runnable {
        @Override
        public void run() {
            Mark.mark();
            System.out.println(Thread.currentThread().getName() + "正在执行");
        }
    }
}
