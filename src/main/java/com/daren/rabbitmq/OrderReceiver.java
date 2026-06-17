//package com.daren.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public class OrderReceiver {
//    private static final Logger log = LoggerFactory.getLogger(OrderReceiver.class);
//
//    @RabbitListener(queues = "simple.queue")
//    public void process(Object message) {
//        log.debug("接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//
//    @RabbitListener(queues = "fanout.queue1")
//    public void process2(String message) {
//        log.debug("fanout1接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//
//    @RabbitListener(queues = "fanout.queue2")
//    public void process3(String message) {
//        log.error("fanout2接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//
//    @RabbitListener(queues = "queue1")
//    public void process4(String message) {
//        log.error("queue1接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//
//    @RabbitListener(queues = "queue2")
//    public void process5(String message) {
//        log.error("queue2接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//
//    @RabbitListener(queues = "1000000.queue")
//    public void process6(String message) {
//        log.debug("1000000.queue接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//        throw new RuntimeException("测试异常");
//    }
//
//    @RabbitListener(queues = "delayed.queue")
//    public void process7(String message) {
//        log.debug("delayed.queue接收到消息：{}", message);
//        // 这里执行后续业务，如扣减库存、异步写入数据库
//    }
//}