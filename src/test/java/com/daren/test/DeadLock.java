package com.daren.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadLock {
    public static void main(String[] args) {
        final Object a = new Object();
        final Object b = new Object();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            synchronized (a) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (b) {

                }
            }
        });
        executorService.submit(() -> {
            synchronized (b) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (a) {

                }
            }
        });
    }

}
