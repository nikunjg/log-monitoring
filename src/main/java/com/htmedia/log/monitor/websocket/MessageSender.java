package com.htmedia.log.monitor.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender<T> {

    /** The template. */
    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(final T message, final String broker) {
        this.template.convertAndSend(broker, message);
    }
}
