package com.example.test;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chengsp
 * @date 2020/5/29 17:20
 */
@Component
public class TestListener implements MessageListenerConcurrently {
    private final static Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            executor.execute(() -> logger.info(new String(msg.getBody())));
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
