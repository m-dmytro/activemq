package com.hw.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.logging.Logger;

@Component
public class Server implements MessageListener {
    Logger logger = Logger.getLogger(Server.class.getName());

    private final boolean transacted = false;
    private static final int ackMode = Session.AUTO_ACKNOWLEDGE;

    private final String messageQueueName;
    private final String messageBrokerUrl;

    private Session session;
    private MessageProducer replyProducer;
    private MessageProcessor messageProcessor;

    @Autowired
    public Server(@Value("${spring.activemq.queue}") String messageQueueName,
                  @Value("${spring.activemq.broker-url}") String messageBrokerUrl) {
        this.messageQueueName = messageQueueName;
        this.messageBrokerUrl = messageBrokerUrl;

        try {
            //This message broker is embedded
            BrokerService broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.addConnector(messageBrokerUrl);
            broker.start();
        } catch (Exception e) {
            //Handle the exception appropriately
        }

        //Delegating the handling of messages to another class, instantiate it before setting up JMS so it
        //is ready to handle messages
        this.messageProcessor = new MessageProcessor();
        this.setupMessageQueueConsumer();
    }

    private void setupMessageQueueConsumer() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            this.session = connection.createSession(this.transacted, ackMode);
            Destination adminQueue = this.session.createQueue(messageQueueName);

            //Setup a message producer to respond to messages from clients, we will get the destination
            //to send to from the JMSReplyTo header field from a Message
            this.replyProducer = this.session.createProducer(null);
            this.replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //Set up a consumer to consume messages off of the admin queue
            MessageConsumer consumer = this.session.createConsumer(adminQueue);
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage response = this.session.createTextMessage();
            String requestMessageText = null;
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                requestMessageText = txtMsg.getText();
                logger.info(message.getJMSCorrelationID() + ": Received message '" + requestMessageText + "' from request chanel " + messageQueueName);
                response.setText(this.messageProcessor.processMessage(requestMessageText));
            }

            //Set the correlation ID from the received message to be the correlation id of the response message
            //this lets the client identify which message this is a response to if it has more than
            //one outstanding message to the server
            response.setJMSCorrelationID(message.getJMSCorrelationID());

            //Send the response to the Destination specified by the JMSReplyTo field of the received message,
            //this is presumably a temporary queue created by the client
            logger.info(message.getJMSCorrelationID() + ": Sending reply to the queue " + message.getJMSReplyTo().toString() + ", based on requested message: '"  + requestMessageText +"'");
            this.replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }
}
