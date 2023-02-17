package com.ShopIT.Service;

import com.ShopIT.Payloads.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @Async
    public void sendGlobalNotification(Message message) {
        messagingTemplate.convertAndSend("/topic/globalNotifications", message);
    }
    @Async
    public void sendPrivateNotification(final Message message) {
        messagingTemplate.convertAndSend("/topic/privateNotifications/"+message.getId(), message);
    }
}
