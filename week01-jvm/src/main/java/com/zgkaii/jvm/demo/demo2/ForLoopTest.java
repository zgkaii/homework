package com.zgkaii.jvm.demo.demo2;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/03/17 23:05
 * @Description:
 **/
public class ForLoopTest {
    private static int[] numbers = {1, 6, 8};
    public static void main(String[] args) {
        MovingAverage ma = new MovingAverage();
        for (int number : numbers) {
            ma.submit(number);
        }
        double avg = ma.getAvg();
    }
}
