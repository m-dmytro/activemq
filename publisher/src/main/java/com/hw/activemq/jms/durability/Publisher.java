package com.hw.activemq.jms.durability;

import cm.hw.activemq.model.MessageObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Publisher {

    Logger logger = Logger.getLogger(Publisher.class.getName());

    @Value("${spring.queues.durability.topic}")
    private String topicName;

    @Autowired
    @Qualifier("topicJmsTemplate")
    private JmsTemplate jmsTopicTemplate;

    public void sendMessageToTopic(MessageObj message) {
        logger.info("Sending message '" + message + "' to topic '" + topicName + "'");
        jmsTopicTemplate.convertAndSend(topicName, message);
    }

}
