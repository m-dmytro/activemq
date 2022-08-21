package com.hw.activemq.jms.durability;

import cm.hw.activemq.model.MessageObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class QueueListener {
    Logger logger = Logger.getLogger(QueueListener.class.getName());

    @Value("${spring.queues.durability.queue}")
    private String queueName;

    @JmsListener(destination = "${spring.queues.durability.queue}", containerFactory = "queueListenerFactory")
    public void receiveFromQueue(MessageObj message) {
        logger.info("Received message '" + message + "' from queue '" + queueName + "'");
    }

}
