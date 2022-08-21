package com.hw.activemq.jms.synccommunication;

import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.logging.Logger;

@Component
public class SyncReplierWithPredefinedQueues {
    Logger logger = Logger.getLogger(SyncReplierWithPredefinedQueues.class.getName());

    @Value("${spring.queues.sync-communication.request-queue}")
    private String requestQueue;

    @Value("${spring.queues.sync-communication.reply-queue}")
    private String replyQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "${spring.queues.sync-communication.request-queue}")
    @SendTo("${spring.queues.sync-communication.reply-queue}")
    public String processRequest(@Payload Message message,
                                 @Header(name = "someHeader") String someHeader) throws JMSException {
        ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
        logger.info("Received message " + textMessage.getText() + " with header 'someHeader:" + someHeader + "' from request chanel " + requestQueue);
        logger.info("Sending reply to the queue " + replyQueue + ", based on requested message: "  + textMessage.getText());
        return "Successfully processed message: " + textMessage.getText();
    }

}
