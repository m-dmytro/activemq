package com.hw.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Random;
import java.util.logging.Logger;

@Component
public class Client implements MessageListener {
    Logger logger = Logger.getLogger(Client.class.getName());

    private final boolean transacted = false;
    private static final int ackMode = Session.AUTO_ACKNOWLEDGE;

    private final String clientQueueName;

    private MessageProducer producer;
    private Session session;
    private Destination tempDest;

    @Autowired
    public Client(@Value("${spring.activemq.queue}") String clientQueueName,
                  @Value("${spring.activemq.broker-url}") String messageBrokerUrl) {
        this.clientQueueName = clientQueueName;

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(messageBrokerUrl);
        Connection connection;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(transacted, ackMode);
            Destination adminQueue = session.createQueue(clientQueueName);

            //Setup a message producer to send message to the queue the server is consuming from
            this.producer = session.createProducer(adminQueue);
            this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //Create a temporary queue that this client will listen for responses on then create a consumer
            //that consumes message from this temporary queue...for a real application a client should reuse
            //the same temp queue for each message to the server...one temp queue per client
            tempDest = session.createTemporaryQueue();
            MessageConsumer responseConsumer = session.createConsumer(tempDest);

            //This class will handle the messages to the temp queue as well
            responseConsumer.setMessageListener(this);

            createAndSendClientMessage("MyStartUpProtocolMessage");
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }

    public void createAndSendClientMessage(String message) throws JMSException {
        //Now create the actual message you want to send
        TextMessage txtMessage = session.createTextMessage();
        txtMessage.setText(message);

        //Set the reply to field to the temp queue you created above, this is the queue the server
        //will respond to
        txtMessage.setJMSReplyTo(tempDest);

        //Set a correlation ID so when you get a response you know which sent message the response is for
        //If there is never more than one outstanding message to the server then the
        //same correlation ID can be used for all the messages...if there is more than one outstanding
        //message to the server you would presumably want to associate the correlation ID with this
        //message somehow...a Map works good
        String correlationId = this.createRandomString();
        txtMessage.setJMSCorrelationID(correlationId);
        logger.info(correlationId + ": Sending request message '" + message + "' to request queue " + clientQueueName);
        this.producer.send(txtMessage);
    }

    private String createRandomString() {
        Random random = new Random(System.currentTimeMillis());
        long randomLong = random.nextLong();
        return Long.toHexString(randomLong);
    }

    @Override
    public void onMessage(Message message) {
        String messageText = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                messageText = textMessage.getText();
                logger.info(message.getJMSCorrelationID() + ": Received reply: '" + messageText + "' from the temp queue");
            }
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }

}
