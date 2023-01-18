package com.ShopIT.Service;
import com.ShopIT.Models.NotificationModel.Notification;
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

    public void sendGlobalNotification() {
        Notification message = new Notification("Users", "Global Notification", null);
        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendPrivateNotification(final String userId) {
        Notification message = new Notification("Users", "Global Notification", null);
        messagingTemplate.convertAndSendToUser(userId,"/topic/private-notifications", message);
    }
}