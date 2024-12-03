package org.livestream.msg.provider.consumer;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.livestream.common.interfaces.topic.ImCoreServerProviderTopicNames;
import org.livestream.framework.mq.starter.properties.RocketMQConsumerProperties;
import org.livestream.im.dto.ImMsgBody;
import org.livestream.msg.provider.consumer.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * @Author idea
 * @Date: Created in 15:04 2023/7/11
 * @Description
 */
@Component
public class ImMsgConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImMsgConsumer.class);
    @Resource
    private RocketMQConsumerProperties rocketMQConsumerProperties;
    @Resource
    private MessageHandler singleMessageHandler;

    // 记录每个用户连接的im服务器地址，然后根据im服务器的连接地址去做具体机器的调用
    // 基于mq广播思路去做，可能会有消息风暴发生，100台im机器，99%的mq消息都是无效的，
    // 加入一个叫路由层的设计，router中转的设计，router就是一个dubbo的rpc层
    // A--》B im-core-server -> msg-provider(持久化) -> im-core-server -> 通知到b
    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultMQPushConsumer mqPushConsumer = new DefaultMQPushConsumer();
        //老版本中会开启，新版本的mq不需要使用到
        mqPushConsumer.setVipChannelEnabled(false);
        mqPushConsumer.setNamesrvAddr(rocketMQConsumerProperties.getNameSrv());
        mqPushConsumer.setConsumerGroup(rocketMQConsumerProperties.getGroupName() + "_" + ImMsgConsumer.class.getSimpleName());
        //一次从broker中拉取10条消息到本地内存当中进行消费
        mqPushConsumer.setConsumeMessageBatchMaxSize(10);
        mqPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //监听im发送过来的业务消息topic
        mqPushConsumer.subscribe(ImCoreServerProviderTopicNames.LIVESTREAM_IM_BIZ_MSG_TOPIC, "");
        mqPushConsumer.setMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                try {
                    ImMsgBody imMsgBody = JSON.parseObject(new String(msg.getBody()), ImMsgBody.class);
                    singleMessageHandler.onMsgReceive(imMsgBody);
                } catch (Exception e) {
                    LOGGER.error("mq 消费者出现异常", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        mqPushConsumer.start();
        LOGGER.info("mq 消费者启动成功, namesrv is {}", rocketMQConsumerProperties.getNameSrv());
    }
}
