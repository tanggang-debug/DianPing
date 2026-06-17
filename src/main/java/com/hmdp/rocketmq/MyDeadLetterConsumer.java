//package com.hmdp.rocketmq;
//
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//@RocketMQMessageListener(
//        topic = "%DLQ%my-consumer-group",          // 监听的话题
//        consumerGroup = "my-dlq-group" // 消费者组
//)
//public class MyDeadLetterConsumer implements RocketMQListener<String> {
//    private static final Logger log = LoggerFactory.getLogger(MyDeadLetterConsumer.class);
//
//    @Override
//    public void onMessage(String message) {
//        log.info("\u001b[31;1m >>> [CRITICAL] deadMessage: {} \u001b[0m", message);
//    }
//}
