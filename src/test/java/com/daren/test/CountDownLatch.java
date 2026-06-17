package com.daren.test;

import java.util.concurrent.CountDownLatch;

 class CountDownLatchTest {

    /**
     * a start signal that prevents any worker from proceeding
     * until the driver is ready for them to proceed;
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        }
        // don't let run yet
        System.out.println("do something else 1");
        // let all threads proceed
        startSignal.countDown();
        System.out.println("do something else 2");
        // wait for all to finish
        doneSignal.await();
        System.out.println("wait for all to finsh");
    }

    static class Worker implements Runnable {

        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        void doWork() {
            System.out.println("do work!");
        }
    }

}
