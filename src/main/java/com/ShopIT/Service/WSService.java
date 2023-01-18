package com.ShopIT.Service;

import com.ShopIT.Models.NotificationModel.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public void notifyFrontend(final String message) {
        Notification response = new Notification("Users", message, null);
        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/messages", response);
    }

    public void notifyUser(final String id, final String message) {
        Notification response = new Notification("Users", message, String.valueOf(id));
        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", response);
    }
}