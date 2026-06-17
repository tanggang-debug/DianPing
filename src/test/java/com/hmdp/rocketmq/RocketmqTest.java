/*
package com.hmdp.rocketmq;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RocketmqTest {
    private static final Logger log = LoggerFactory.getLogger(RocketmqTest.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
//    @Test
//    void testSendMessage() throws ClientException, IOException {
//        // 1. 配置 Endpoint (通常是 Proxy 的地址)
//        String endpoint = "localhost:8081";
//        String topic = "MyFirstTopic";
//        ClientConfiguration configuration = ClientConfiguration.newBuilder()
//                .setEndpoints(endpoint)
//                .build();
//
//        // 2. 初始化 Producer
//        ClientServiceProvider provider = ClientServiceProvider.loadService();
//        Producer producer = provider.newProducerBuilder()
//                .setTopics(topic)
//                .setClientConfiguration(configuration)
//                .build();
//
//        // 3. 构建并发送消息
//        Message message = provider.newMessageBuilder()
//                .setTopic(topic)
//                .setKeys("Key001")
//                .setBody("Hello RocketMQ 5.x".getBytes())
//                .build();
//
//        SendReceipt sendReceipt = producer.send(message);
//        System.out.println("Message sent successfully, messageId=" + sendReceipt.getMessageId());
//        producer.close();
//    }
//
//    @Test
//    void getMessage() throws ClientException, IOException, InterruptedException {
//        String endpoint = "localhost:8081";
//        String topic = "MyFirstTopic";
//        ClientConfiguration configuration = ClientConfiguration.newBuilder()
//                .setEndpoints(endpoint)
//                .build();
//
//        ClientServiceProvider provider = ClientServiceProvider.loadService();
//        // 初始化 PushConsumer
//        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
//                .setClientConfiguration(configuration)
//                .setConsumerGroup("YourConsumerGroup")
//                .setSubscriptionExpressions(Collections.singletonMap(topic, FilterExpression.SUB_ALL))
//                .setMessageListener(messageView -> {
//                    System.out.println("Consume message: " + messageView);
//                    System.out.println("Consume message: " + StandardCharsets.UTF_8.decode(messageView.getBody()).toString());
//                    return ConsumeResult.SUCCESS;
//                })
//                .build();
//
//        // 阻塞主线程以保持消费，实际应用中由应用生命周期管理
//        Thread.sleep(Long.MAX_VALUE);
//    }

    @Test
    void testSendMessageByTemplate() {

        // 2. 发送带 Key 的消息（方便在 Dashboard 以后置查询）
        ArrayList<Message<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message<String> msg = MessageBuilder.withPayload("Order Data")
                    .setHeader(RocketMQHeaders.KEYS, "ORDER_ID_001")
                    .build();
            list.add(msg);
        }
        rocketMQTemplate.syncSend("MyFirstTopic", list, 3000);
    }

    @Test
    void testSendMessageByTemplate2() {
        ArrayList<Message<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message<String> msg = MessageBuilder.withPayload("Order Data")
                    .setHeader(RocketMQHeaders.TAGS, "tag1")
                    .build();
            list.add(msg);
        }
        for (Message<String> msg : list) {
            SendResult sendResult = rocketMQTemplate.syncSendOrderly("MyFirstTopic", msg, "1");
            System.out.println("消息已发送至:" + sendResult.getMessageQueue().getQueueId());
        }
    }

    @Test
    void testSendMessageRetry() {
        Message<String> msg = MessageBuilder.withPayload("hello world!")
                .build();
        SendResult sendResult = rocketMQTemplate.syncSend("MyFirstTopic:tag1", msg);
    }
}
*/
