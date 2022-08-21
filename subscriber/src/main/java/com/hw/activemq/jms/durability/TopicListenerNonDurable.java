package com.hw.activemq.jms.durability;

import cm.hw.activemq.model.MessageObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class TopicListenerNonDurable {
    Logger logger = Logger.getLogger(TopicListenerNonDurable.class.getName());

    @Value("${spring.queues.durability.topic}")
    private String topicName;

    @JmsListener(destination = "${spring.queues.durability.topic}", containerFactory = "topicListener2Factory")
    public void receiveFromTopic(MessageObj message) {
        logger.info("Received message '" + message + "' from topic '" + topicName + "' by non-durable subscriber");
    }

}
