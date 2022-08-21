package com.hw.activemq.jms;

import cm.hw.activemq.model.MessageObj;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Publisher {

    Logger logger = Logger.getLogger(Publisher.class.getName());

    @Value("${spring.queue}")
    private String virtualTopic;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessageToQueues(MessageObj message) {
        logger.info("Sending message '" + message + "' to virtual topic '" + virtualTopic + "'");
        jmsTemplate.convertAndSend(new ActiveMQTopic(virtualTopic), message);
    }

}
