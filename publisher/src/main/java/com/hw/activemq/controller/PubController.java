package com.hw.activemq.controller;

import cm.hw.activemq.model.MessageObj;
import com.hw.activemq.jms.durability.Producer;
import com.hw.activemq.jms.durability.Publisher;
import com.hw.activemq.jms.synccommunication.SyncRequestor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/send")
public class PubController {
    Logger logger = Logger.getLogger(PubController.class.getName());

    @Autowired
    Publisher publisher;

    @Autowired
    Producer producer;

    @Autowired
    SyncRequestor syncRequestor;

    @Autowired
    ActiveMQConnectionFactory connectionFactory;

    @PostMapping("/{message}")
    public void sendMessageToQueue(@PathVariable("message") String message) {
        logger.info("Check messages durability");
        logger.info("Message received at the endpoint: " + message);
        MessageObj messageObj = new MessageObj(String.valueOf(UUID.randomUUID()), message);
        producer.sendMessageToQueue(messageObj);
        publisher.sendMessageToTopic(messageObj);
    }

    @PostMapping("/syncqueues/{message}")
    public void sendSyncMessage(@PathVariable("message") String message) {
        logger.info("Check sync communications between producer and consumer: wait for the consumer to process the message and return a reply");
        logger.info("Message received at the endpoint: " + message);
        syncRequestor.processSyncMessageWithPredefinedQueues(new MessageObj(String.valueOf(UUID.randomUUID()), message));
    }

}
