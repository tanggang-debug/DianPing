package com.daren.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

 class ConditionVariableExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean available = false;

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (!available) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            available = true;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
