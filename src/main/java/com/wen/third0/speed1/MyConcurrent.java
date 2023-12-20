package com.wen.third0.speed1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author: 7wen
 * @Date: 2023/12/20 10:03
 * @description: 并发计算与串行计算的速度比较
 *
 * 并发计算采用主线程+子线程计算
 * 串行计算采用主线程计算
 *
 * 采样计算量达到1e以下的时候 并发计算速度小于串行计算
 * 采样计算量达到10e以上的时候 并发计算速度大于串行计算
 *
 * 所以并发计算不一定比串行计算快
 * 并发计算呈抛物线式增长,串行计算为直线下降
 */
public class MyConcurrent {
    //1e concurrent 27ms serial 14ms
    static final long COUNT = 100000000L;

    //10e concurrent 76ms serial 113ms
//    static final long COUNT = 1000000000L;

    public static void main(String[] args) {
        concurrentCalc();
        serializationCalc();
    }

    private static void serializationCalc() {
        long beginTime = System.currentTimeMillis();

        long i;
        for (i = 0; i < COUNT; i++) {
            i += 3;
        }

        long j;
        for (j = 0; j < COUNT; j++) {
            j += 3;
        }

        System.out.println(
                "serialization useTime -> " + (System.currentTimeMillis() - beginTime) + "ms" +
                        " and mainThread i result -> " + i +
                        " ;mainThread j result -> " + j);

    }

    private static void concurrentCalc() {
        //声明开始时间
        long startTime = System.currentTimeMillis();

        FutureTask<Long> task = new FutureTask<Long>(new Callable() {
            @Override
            public Long call() throws Exception {
                long i;
                for (i = 0; i < COUNT; i++) {
                    i += 3;
                }
                return i;
            }
        });

        Thread thread = new Thread(task);
        //并发线程开始计算
        thread.start();

        //主线程开始计算
        long i;
        for (i = 0; i < COUNT; i++) {
            i += 3;
        }

        //主线程计算完毕
        try {
            //计算时间
            Long result = task.get();
            long endTime = System.currentTimeMillis();
            System.out.println(
                    "concurrent useTime -> " + (endTime - startTime) + "ms" +
                            " and thread result -> " + result +
                            " ;mainThread result -> " + i);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
