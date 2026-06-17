//package com.daren.rabbitmq;
//
//import cn.hutool.core.map.MapBuilder;
//import com.daren.HmDianPingApplication;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.AmqpException;
//import org.springframework.amqp.core.Correlation;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessagePostProcessor;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ApplicationContext;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class OrderReceiverTest {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    private static final Logger log = LoggerFactory.getLogger(OrderReceiver.class);
//    @Autowired
//    RabbitProducer rabbitProducer;
//
//    @Test
//    void testRabbitMQ() {
//        // 发送到交换机，指定路由键
//        Map<Object, Object> map = MapBuilder.create().put("name", "zhangsan").build();
//        rabbitTemplate.convertAndSend("topic.exchange", "order.order", map);
//    }
//
//    @Test
//    void testRabbitCallback() {
//        rabbitProducer.sendAsync("order:1", "hello world!");
//    }
//
//    @Test
//    void test1000000Message() {
//        rabbitTemplate.convertAndSend("1000000.queue", "message");
//    }
//
//    @Test
//    void testDelayedMessage() {
//        String message = "hello, delayed message";
//        rabbitTemplate.convertAndSend("delayed.exchange", "delayed", message, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                message.getMessageProperties().setDelay(20000);
//                return message;
//            }
//        });
//    }
//
//}