package com.wen.third0.speed0;

import co.paralleluniverse.fibers.Fiber;

import java.util.concurrent.ExecutionException;

import static com.wen.third0.speed0.MyThread.THREAD_LENGTH;

/**
 * @author: 7wen
 * @Date: 2023/12/19 17:12
 * @description:
 * 当线程/协程数达到 5w 的时候计算 5w*200w数据的时候
 * 线程因为要调用内核态所以资源消耗极大;协程只在用户态中执行,所以几乎没有什么消耗
 *
 * 执行5w个线程 是实实在在的有5w个线程切换内核态使用资源
 * 执行5w个协程 分配了15个线程 也就是说只有15个线程在切换内核分配资源;而实际干活的协程又对内核透明,所以很快
 *
 * cpu:i5-13500H
 * 内存:16g
 *
 * 线程用时:26543ms
 * 协程用时:504ms
 *
 * 执行速度差异极大
 */
public class MyFiber {

    public static void main(String[] args) {
        //记时开始
        Long begin = System.currentTimeMillis();
        int result = 0;

        //声明协程数组
        Fiber<Void>[] fibers = new Fiber[THREAD_LENGTH];

        for (int i = 0; i < THREAD_LENGTH; i++) {
            fibers[i] = new Fiber<Void>(() -> {
                calc(result);
            });
        }

        //线程数组里的线程开始执行任务
        for (int i = 0; i < THREAD_LENGTH; i++) {
            fibers[i].start();
        }

        //线程数组里的线程等待执行完成后该main函数结束
        for (int i = 0; i < THREAD_LENGTH; i++) {
            try {
                fibers[i].join();
            } catch (ExecutionException | InterruptedException e) {
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
