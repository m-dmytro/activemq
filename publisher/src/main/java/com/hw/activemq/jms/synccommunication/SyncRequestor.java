package com.hw.activemq.jms.synccommunication;

import cm.hw.activemq.model.MessageObj;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.logging.Logger;

@Component
public class SyncRequestor {
    Logger logger = Logger.getLogger(SyncRequestor.class.getName());

    @Value("${spring.queues.sync-communication.request-queue}")
    private String requestQueue;
    @Value("${spring.queues.sync-communication.reply-queue}")
    private String replyQueue;

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    public ActiveMQConnectionFactory connectionFactory;

    public void processSyncMessageWithPredefinedQueues(MessageObj message) {
        logger.info("Sending request to request queue " + requestQueue);
        jmsTemplate.convertAndSend(requestQueue, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("someHeader", "randomHeaderValue");
                return message;
            }
        });
        Object replyObject = jmsTemplate.receiveAndConvert(replyQueue);
        logger.info("Received reply: " + replyObject + "from the queue " + replyQueue);
    }

}
