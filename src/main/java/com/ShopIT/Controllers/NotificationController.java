package com.ShopIT.Controllers;

import com.ShopIT.Models.NotificationModel.Notification;
import com.ShopIT.Models.User;
import com.ShopIT.Security.CurrentUser;
import com.ShopIT.Service.NotificationService;
import com.ShopIT.Service.WSService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/notification")
public class NotificationController {
    private final WSService service;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationService notificationService;
    //Mapped as /app/application
    //The message mapping ensure if the message is sent to the /application send() method is called
    //here it will require to take Notification Json Model as input which will get broadcast
    @MessageMapping("/message")
    //send to will help to subscribe to the websocket communication channel
    @SendTo("/topic/messages")
    public Notification send(@RequestBody Notification message) throws Exception{
        notificationService.sendGlobalNotification();
        return message;
    }
    //Mapped as /app/specific
    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public void sendToSingleUser(@CurrentUser User user, @Payload Notification message){
        notificationService.sendPrivateNotification(user.getFirstname());
//        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/send-message")
    public void sendMessage(@CurrentUser User user, @RequestBody final Notification message) {
        service.notifyFrontend(message.getMessage());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final Notification message) {
        service.notifyUser(id, message.getMessage());
    }

}
