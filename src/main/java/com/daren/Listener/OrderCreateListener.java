//package com.hmdp.Listener;
//
//import cn.hutool.json.JSONUtil;
//import com.hmdp.entity.VoucherOrder;
//import com.hmdp.service.impl.VoucherOrderServiceImpl;
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
//        topic = "orderCreateTopic",          // 监听的话题
//        consumerGroup = "orderCreate-consumer-group" // 消费者组
//        , selectorType = TAG,
//        maxReconsumeTimes = 5  // 直接在这里写死
//)
//public class OrderCreateListener implements RocketMQListener<String> {
//    private static final Logger log = LoggerFactory.getLogger(com.hmdp.rocketmq.MyDeadLetterConsumer.class);
//    private final VoucherOrderServiceImpl voucherOrderServiceImpl;
//
//    public OrderCreateListener(VoucherOrderServiceImpl voucherOrderServiceImpl) {
//        this.voucherOrderServiceImpl = voucherOrderServiceImpl;
//    }
//
//    @Override
//    public void onMessage(String message) {
//        VoucherOrder voucherOrder = JSONUtil.toBean(message, VoucherOrder.class);
////        voucherOrderServiceImpl.handleVoucherOrder(voucherOrder);
//    }
//}
////public class OrderCreateListener {
////
////    private final VoucherOrderServiceImpl voucherOrderServiceImpl;
////
////    public OrderCreateListener(VoucherOrderServiceImpl voucherOrderServiceImpl) {
////        this.voucherOrderServiceImpl = voucherOrderServiceImpl;
////    }
////
////    @RabbitListener(queues = "order.create.queue", concurrency = "5-10")
////    // concurrency="5-10" 表示开启 5 到 10 个线程并行处理，提升 TPS
////    public void onOrderCreateMessage(VoucherOrder voucherOrder) {
////        voucherOrderServiceImpl.handleVoucherOrder(voucherOrder);
////    }
////}
