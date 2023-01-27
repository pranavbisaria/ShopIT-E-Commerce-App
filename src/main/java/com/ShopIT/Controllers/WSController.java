package com.ShopIT.Controllers;
import com.ShopIT.Payloads.Message;
import com.ShopIT.Service.WSService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class WSController {
    private final WSService service;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/globalNotification")
    public void sendNotificationToAll(@RequestBody final Message message) {
        service.notifyFrontend(message);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/privateNotification/{id}")
    public void sendPrivateNotification(@PathVariable final String id,
                                        @RequestBody final Message message) {
        message.setId(id);
        service.notifyUser(message);
    }
}
