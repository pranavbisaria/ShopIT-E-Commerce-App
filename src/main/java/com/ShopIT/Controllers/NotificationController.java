package com.ShopIT.Controllers;

import com.ShopIT.Models.NotificationModel.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/notification")
public class NotificationController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/application")
    @SendTo("/all/return")
    public Notification send(@RequestBody Notification message) throws Exception{
        return message;
    }

//    @MessageMapping("/specific")
//    public void sendToSingleUser(@Payload Message message){
//        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
//    }
}
