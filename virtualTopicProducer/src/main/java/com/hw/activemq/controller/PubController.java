package com.hw.activemq.controller;

import cm.hw.activemq.model.MessageObj;
import com.hw.activemq.jms.Publisher;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/publisher/virtualtopic")
public class PubController {
    Logger logger = Logger.getLogger(PubController.class.getName());

    @Autowired
    Publisher publisher;

    @Autowired
    ActiveMQConnectionFactory connectionFactory;

    @PostMapping("/{message}")
    public void sendMessageToQueue(@PathVariable("message") String message) {
        logger.info("Check messages durability");
        logger.info("Message received at the endpoint: " + message);
        publisher.sendMessageToQueues(new MessageObj(String.valueOf(UUID.randomUUID()), message));
    }

}
