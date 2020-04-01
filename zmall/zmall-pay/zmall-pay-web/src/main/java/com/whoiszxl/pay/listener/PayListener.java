package com.whoiszxl.pay.listener;

import com.aliyun.mq.http.MQClient;
import com.aliyun.mq.http.MQConsumer;
import com.aliyun.mq.http.common.AckMessageException;
import com.aliyun.mq.http.model.Message;
import com.whoiszxl.common.config.MqTopicEnums;
import com.whoiszxl.common.config.RocketMQConfig;
import com.whoiszxl.common.listener.RocketMQSender;
import com.whoiszxl.pay.entity.PaymentResponseDTO;
import com.whoiszxl.pay.entity.TradeStatus;
import com.whoiszxl.pay.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品
 */
@Component
@Slf4j
public class PayListener {

    @Value("${aliyun.accountEndpoint}")
    private String accountEndpoint;

    @Value("${aliyun.accessId}")
    private String accessId;

    @Value("${aliyun.accessKey}")
    private String accessKey;

    /** 所属的 Topic */
    @Value("${rocketmq.topic}")
    private String topic;

    /** Topic所属实例ID，默认实例为空 */
    @Value("${rocketmq.instanceId}")
    private String instanceId;

    /** 所属的 Topic */
    @Value("${rocketmq.groupId}")
    private String groupId;

    @Autowired
    private PayService payService;

    @Autowired
    private RocketMQSender rocketMQSender;


    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void execute() {
        MQClient mqClient = new MQClient(
                // 设置HTTP接入域名（此处以公共云生产环境为例）
                accountEndpoint,
                // AccessKey 阿里云身份验证，在阿里云服务器管理控制台创建
                accessId,
                // SecretKey 阿里云身份验证，在阿里云服务器管理控制台创建
                accessKey
        );

        final MQConsumer consumer;
        if (instanceId != null && instanceId != "") {
            consumer = mqClient.getConsumer(instanceId, topic, groupId, null);
        } else {
            consumer = mqClient.getConsumer(topic, groupId);
        }



        // 在当前线程循环消费消息，建议是多开个几个线程并发消费消息
        do {
            List<Message> messages = null;

            try {
                // 长轮询消费消息
                // 长轮询表示如果topic没有消息则请求会在服务端挂住3s，3s内如果有消息可以消费则立即返回
                messages = consumer.consumeMessage(
                        3,// 一次最多消费3条(最多可设置为16条)
                        3// 长轮询时间3秒（最多可设置为30秒）
                );
            } catch (Throwable e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            // 没有消息
            if (messages == null || messages.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + ": no new message, continue!");
                continue;
            }

            // 处理业务逻辑
            for (Message message : messages) {
                String messageTag = message.getMessageTag();
                if(messageTag.equals(RocketMQConfig.PAY_QUERY)) {
                    String orderId = message.getMessageBodyString();
                    PaymentResponseDTO responseDTO = payService.queryPayOrderByAli(orderId);

                    //当没有获取到订单结果，抛出异常，再次重试消费
                    if(responseDTO == null || TradeStatus.UNKNOWN.equals(responseDTO.getTradeState()) || TradeStatus.USERPAYING.equals(responseDTO.getTradeState()) ){
                        throw new RuntimeException("支付状态未知，等待重试");
                    }

                    //如果重试的次数达到一次数量，不要再重试消费，将消息记录到数据库，由单独的程序或人工进行处理
                    //将订单状态，再次发到mq...
                    rocketMQSender.send(MqTopicEnums.PAY_RESULT_TOPIC.getName(), responseDTO.getOutTradeNo() + ":::::" + responseDTO.getTradeNo());
                }
                System.out.println("Receive message: " + message);
            }

            // Message.nextConsumeTime前若不确认消息消费成功，则消息会重复消费
            // 消息句柄有时间戳，同一条消息每次消费拿到的都不一样
            {
                List<String> handles = new ArrayList<String>();
                for (Message message : messages) {
                    handles.add(message.getReceiptHandle());
                }

                try {
                    consumer.ackMessage(handles);
                } catch (Throwable e) {
                    // 某些消息的句柄可能超时了会导致确认不成功
                    if (e instanceof AckMessageException) {
                        AckMessageException errors = (AckMessageException) e;
                        System.out.println("Ack message fail, requestId is:" + errors.getRequestId() + ", fail handles:");
                        if (errors.getErrorMessages() != null) {
                            for (String errorHandle :errors.getErrorMessages().keySet()) {
                                System.out.println("Handles:" + errorHandle + ", ErrorCode:" + errors.getErrorMessages().get(errorHandle).getErrorCode()
                                        + ", ErrorMsg:" + errors.getErrorMessages().get(errorHandle).getErrorMessage());
                            }
                        }
                        continue;
                    }
                    e.printStackTrace();
                }
            }
        } while (true);
    }
}
