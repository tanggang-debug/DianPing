//package com.hmdp.rocketmq;
//
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import static org.apache.rocketmq.spring.annotation.SelectorType.TAG;
//
//@Component
//@RocketMQMessageListener(
//        topic = "MyFirstTopic",          // 监听的话题
//        consumerGroup = "my-consumer-group" // 消费者组
//        , selectorType = TAG,
//        selectorExpression = "tag1",
//        maxReconsumeTimes = 1  // 直接在这里写死
//)
//public class MyConsumer implements RocketMQListener<String> {
//    private static final Logger log = LoggerFactory.getLogger(MyConsumer.class);
//
//    @Override
//    public void onMessage(String message) {
//        log.info("\u001b[31;1m >>> [CRITICAL] message: {} \u001b[0m", message);
//        throw new RuntimeException("测试异常");
//    }
//}
