package com.ShopIT.Service;

import com.ShopIT.Payloads.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public void sendGlobalNotification(Message message) {
        messagingTemplate.convertAndSend("/topic/globalNotifications", message);
    }
    public void sendPrivateNotification(final Message message) {
        messagingTemplate.convertAndSend("/topic/privateNotifications/"+message.getId(), message);
    }
}
