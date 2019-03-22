package com.papaya.thread;

import android.net.wifi.aware.PublishConfig;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.papaya.thread.producer.Consumer;
import com.papaya.thread.producer.Producer;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 生产者消费者模式
     *
     * @param view
     */
    public void producer(View view) {
        Vector vector = new Vector();
        Producer producer = new Producer(vector, 5);
        Consumer consumer = new Consumer(vector);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
