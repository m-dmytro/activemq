package com.hw.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/tempQueueClient")
public class ClientController {

    Logger logger = Logger.getLogger(ClientController.class.getName());

    @Autowired
    Client client;

    @PostMapping("/{message}")
    public void sendMessageToQueue(@PathVariable("message") String message) throws JMSException {
        logger.info("Check sync communication with temporary reply queue");
        logger.info("Message received at the endpoint: '" + message +"'");
        client.createAndSendClientMessage(message);
    }

}
