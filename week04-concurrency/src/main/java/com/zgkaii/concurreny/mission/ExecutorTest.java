package com.zgkaii.concurreny.mission;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/08 23:40
 * @Description:
 **/
public class ExecutorTest {
    public static void main(String[] args) {
        // 1.创建线程池对象
        ExecutorService poo1 = Executors.newFixedThreadPool(2);
        // 2.创建Runnable2实例对象
        MyRunnable2 mr = new MyRunnable2();
        // 3.从线程池中获取线程对象，调用MyRunnable中的run()
        poo1.submit(mr);
        // 4.再获取个线程对象，调用MyRunnable中的run()
        poo1.submit(mr);
        // 5.关闭线程池
        poo1.shutdown();
    }
}
