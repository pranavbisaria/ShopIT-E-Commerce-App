package com.ShopIT.Controllers;
import com.ShopIT.Payloads.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
@Controller
@RequiredArgsConstructor
public class MessageController {
    @MessageMapping("/globalNotification")
    @SendTo("/topic/globalNotifications")
    public Message getNotification(Message message) throws InterruptedException {
        Thread.sleep(1000);
        return new Message(HtmlUtils.htmlEscape(message.getHead()), HtmlUtils.htmlEscape(message.getBody()), HtmlUtils.htmlEscape(message.getImage()), null);
    }
    @MessageMapping("/privateNotification")
    @SendTo("/topic/privateNotifications/{userId}")
    public Message getPrivateNotification(final Message message, Long userId) throws InterruptedException {
        Thread.sleep(1000);
        return new Message(HtmlUtils.htmlEscape(
                "Sending private message to user with UserID" + userId + ": "
                        +message.getHead()), HtmlUtils.htmlEscape(message.getBody()), HtmlUtils.htmlEscape(message.getImage()), null);
    }
}
