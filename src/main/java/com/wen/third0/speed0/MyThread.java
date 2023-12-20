package com.wen.third0.speed0;

/**
 * @author: 7wen
 * @Date: 2023/12/19 17:14
 * @description:
 */
public class MyThread {
//    static int THREAD_LENGTH = 100000;
    static int THREAD_LENGTH = 50000;

    public static void main(String[] args) {
        Long begin = System.currentTimeMillis();
        int result = 0;

        Thread[] threads = new Thread[THREAD_LENGTH];

        for (int i = 0; i < THREAD_LENGTH; i++) {
            threads[i] = new Thread(() -> {
                calc(result);
            });
        }

        //线程数组里的线程开始执行任务
        for (int i = 0; i < THREAD_LENGTH; i++) {
            threads[i].start();
        }

        //线程数组里的线程等待执行完成后该main函数结束
        for (int i = 0; i < THREAD_LENGTH; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.currentTimeMillis() - begin);
        System.out.println("result=" + result);

    }

    private static int calc(int result) {
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 200; j++) {
                result += 1;
            }
        }
        return result;
    }
}
