package com.papaya.thread.producer;


import android.util.Log;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者和消费者
 * 其实逻辑并不难，概括起来就两句话：
 * 如果生产者的队列满了(while循环判断是否满)，则等待。如果生产者的队列没满，则生产数据并唤醒消费者进行消费。
 * 如果消费者的队列空了(while循环判断是否空)，则等待。如果消费者的队列没空，则消费数据并唤醒生产者进行生产。
 * <p>
 * 生产者
 */
public class Producer implements Runnable {
    private String TAG = Producer.class.getName();

    // true--->生产者一直执行，false--->停掉生产者
    private volatile boolean isRunning = true;

    // 公共资源
    private final Vector sharedQueue;

    // 公共资源的最大数量
    private final int SIZE;

    // 生产数据
    private static AtomicInteger count = new AtomicInteger();

    public Producer(Vector sharedQueue, int SIZE) {
        this.sharedQueue = sharedQueue;
        this.SIZE = SIZE;
    }

    @Override
    public void run() {
        int data;
        Random r = new Random();

        Log.i(TAG, "start producer id = " + Thread.currentThread().getId());
        try {
            while (isRunning) {
                // 模拟延迟
                Thread.sleep(r.nextInt(1000));

                // 当队列满时阻塞等待
                while (sharedQueue.size() == SIZE) {
                    synchronized (sharedQueue) {
                        Log.i(TAG, "Queue is full, producer " + Thread.currentThread().getId()
                                + " is waiting, size：" + sharedQueue.size());
                        sharedQueue.wait();//生产者的队列满了(while循环判断是否满)，则等待
                    }
                }

                // 队列不满时持续创造新元素
                synchronized (sharedQueue) {
                    // 生产数据
                    data = count.incrementAndGet();
                    sharedQueue.add(data);

                    Log.i(TAG, "producer create data:" + data + ", size：" + sharedQueue.size());
                    sharedQueue.notifyAll();//唤醒消费者进行消费
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupted();
        }
    }

    public void stop() {
        isRunning = false;
    }
}
