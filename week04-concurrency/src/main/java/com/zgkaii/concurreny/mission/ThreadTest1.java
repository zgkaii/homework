package com.zgkaii.concurreny.mission;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/06 10:53
 * @Description:
 */
public class ThreadTest1 {
    public static void main(String[] args) {
        System.out.println("主线程开始执行");
        // 创建自定义线程对象
        MyThreadOne mt = new MyThreadOne("子线程");
        // 启动新线程
        mt.start();

//        System.out.println("主线程开始执行");
//        new Thread() {
//            @Override
//            public void run() {
//                Mark.mark();
//                System.out.println(getName() + "正在执行: " + this);
//            }
//        }.start();
    }

    public static class MyThreadOne extends Thread {
        public MyThreadOne(String name) {
            super(name);
        }

        @Override
        public void run() {
            Mark.mark();
            System.out.println(getName() + "正在执行: " + this);
        }
    }
}
