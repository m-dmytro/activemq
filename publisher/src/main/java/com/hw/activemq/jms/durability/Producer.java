package com.hw.activemq.jms.durability;

import cm.hw.activemq.model.MessageObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Producer {

    Logger logger = Logger.getLogger(Producer.class.getName());

    @Value("${spring.queues.durability.queue}")
    private String queueName;

    @Autowired
    @Qualifier("queueJmsTemplate")
    private JmsTemplate jmsQueueTemplate;

    public void sendMessageToQueue(MessageObj message) {
        /* Queue is always DURABLE */
        logger.info("Sending message '" + message + "' to queue '" + queueName + "'");
        jmsQueueTemplate.convertAndSend(queueName, message);
    }

}
