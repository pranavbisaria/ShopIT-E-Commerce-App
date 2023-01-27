package com.ShopIT.Service;
import com.ShopIT.Payloads.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class WSService {
    private final NotificationService notificationService;
    public void notifyFrontend(final Message message) {
        notificationService.sendGlobalNotification(message);
    }
    public void notifyUser(final Message message) {
        notificationService.sendPrivateNotification(message);
    }
}
