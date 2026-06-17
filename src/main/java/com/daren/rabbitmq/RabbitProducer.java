//package com.hmdp.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RabbitProducer {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    Logger log = LoggerFactory.getLogger(RabbitProducer.class);
//    public void sendAsync(String businessId, Object message) {
//        // 1. 封装关联数据（用于异步追踪消息状态）
//        CorrelationData correlationData = new CorrelationData(businessId);
//
//        // 2. 注册异步回调监听
//        correlationData.getFuture().addCallback(
//            result -> {
//                // 当 Broker 返回 Ack/Nack 时，此逻辑在 RabbitMQ 回调线程池中执行
//                if (result != null && result.isAck()) {
//                    log.info("消息异步确认成功: ID = {}", businessId);
//                } else {
//                    log.error("消息异步确认失败（Nack）: ID = {}, 原因: {}",
//                              businessId, result != null ? result.getReason() : "未知");
//                    // 此处可以执行重发逻辑或记录到异常表
//                }
//            },
//            ex -> {
//                // 当发生网络异常、Broker 宕机等无法收到确认的情况时触发
//                log.error("消息确认过程发生异常: ID = {}, 异常信息: {}", businessId, ex.getMessage());
//            }
//        );
//
//        // 3. 发送消息（主线程立即返回，不会阻塞等待确认）
//        rabbitTemplate.convertAndSend("topic.exchange1", "order.order", message, correlationData);
//
//        log.info("主线程已完成发送逻辑，业务 ID: {}", businessId);
//    }
//}