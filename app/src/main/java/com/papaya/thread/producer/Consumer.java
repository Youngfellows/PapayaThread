package com.papaya.thread.producer;

import android.util.Log;

import java.util.Random;
import java.util.Vector;

/**
 * 生产者和消费者
 * 其实逻辑并不难，概括起来就两句话：
 * 如果生产者的队列满了(while循环判断是否满)，则等待。如果生产者的队列没满，则生产数据并唤醒消费者进行消费。
 * 如果消费者的队列空了(while循环判断是否空)，则等待。如果消费者的队列没空，则消费数据并唤醒生产者进行生产。
 * <p>
 * 消费者
 */
public class Consumer implements Runnable {
    private String TAG = Consumer.class.getName();
    // 公共资源
    private final Vector sharedQueue;

    public Consumer(Vector sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        Random r = new Random();
        Log.d(TAG, "start consumer id = " + Thread.currentThread().getId());
        try {
            while (true) {
                // 模拟延迟
                Thread.sleep(r.nextInt(1500));

                // 当队列空时阻塞等待
                while (sharedQueue.isEmpty()) {
                    synchronized (sharedQueue) {
                        Log.d(TAG, "Queue is empty, consumer " + Thread.currentThread().getId()
                                + " is waiting, size：" + sharedQueue.size());
                        sharedQueue.wait();//消费者的队列空了(while循环判断是否空)，则等待
                    }
                }
                // 队列不空时持续消费元素
                synchronized (sharedQueue) {
                    //消费数据
                    Log.d(TAG, "consumer consume data：" + sharedQueue.remove(0) + ", size：" + sharedQueue.size());
                    sharedQueue.notifyAll();//如果消费者的队列没空，则消费数据并唤醒生产者进行生产
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
