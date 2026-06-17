package com.daren.test;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列示例
 */
public class DelayQueueDemo implements Delayed {

    public static void main(String[] args) {
        DelayQueue<DelayQueueDemo> delayQueue = new DelayQueue<>(); // 创建延迟队列
        delayQueue.add(new DelayQueueDemo()); // 添加元素
        delayQueue.add(new DelayQueueDemo()); // 添加元素
        delayQueue.add(new DelayQueueDemo()); // 添加元素
        System.out.println(System.currentTimeMillis() + "  添加完成"); // 打印添加完成信息
        while (true) { // 循环获取元素
            DelayQueueDemo demo = delayQueue.poll(); // 获取元素
            if (demo != null) { // 如果元素不为空
                System.out.println(System.currentTimeMillis() + "  取出成功"); // 打印取出成功信息
            }
        }
    }

    /**
     * 构造方法, 初始化延迟时间, 设置为 3000 毫秒
     */
    public DelayQueueDemo() {
        this.timestamp = System.currentTimeMillis() + 3000;
    }

    /**
     * 对象到期时间, 单位毫秒, 超过到期时间则能被取出
     */
    long timestamp;

    /**
     * 获取延迟时间, 单位毫秒, 超过到期时间则能被取出
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return this.timestamp - System.currentTimeMillis();
    }

    /**
     * 比较方法, 用于排序, 按照到期时间升序排列
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.timestamp - ((DelayQueueDemo) o).timestamp);
    }
}