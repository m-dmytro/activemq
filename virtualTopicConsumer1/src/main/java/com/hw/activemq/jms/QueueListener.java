package com.hw.activemq.jms;

import cm.hw.activemq.model.MessageObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class QueueListener {

    Logger logger = Logger.getLogger(QueueListener.class.getName());

    @Value("${spring.queue}")
    private String virtualTopic;

    @JmsListener(destination = "${spring.queue}")
    public void receive(MessageObj message) {
        logger.info("Received message '" + message + "' from virtual topic '" + virtualTopic + "'");
    }

}
