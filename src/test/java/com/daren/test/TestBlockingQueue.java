package com.daren.test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestBlockingQueue {
    public static void main(String[] args) {
        PriorityBlockingQueue<Runnable> tasks = new PriorityBlockingQueue<>();
        for (int i = 10; i >= 0; i--) {
            tasks.add(new MyTask(i));
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, tasks);
        threadPoolExecutor.prestartAllCoreThreads();
        threadPoolExecutor.shutdown();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class MyTask implements Runnable, Comparable<MyTask>, Callable<MyTask> {
    int p;
    static AtomicInteger counter = new AtomicInteger(0);
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public MyTask(int p) {
        this.p = p;
    }

    @Override
    public void run() {
        lock.lock();
        while (p != counter.get()) {
            try {
                condition.signal();
                condition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        counter.getAndIncrement();
        condition.signal();
        System.out.println(p);
        lock.unlock();
    }

    @Override
    public int compareTo(MyTask o) {
        return Integer.compare(this.p, o.p);
    }

    @Override
    public MyTask call() throws Exception {
        return new MyTask(100);
    }
}
