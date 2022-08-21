package com.hw.activemq;

public class MessageProcessor {

    public String processMessage(String messageText) {
        String responseText;
        if ("MyStartUpProtocolMessage".equalsIgnoreCase(messageText)) {
            responseText = "It's startup message. Connection established successfully";
        } else {
            responseText = "Processed message: " + messageText;
        }
        return responseText;
    }

}
