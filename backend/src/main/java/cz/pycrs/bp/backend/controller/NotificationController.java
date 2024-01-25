package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.notification.dto.NotificationDetail;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(value = "/live", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter liveNotifications(Authentication authentication) {
        return notificationService.createEmitter((User) authentication.getPrincipal());
    }

    @GetMapping
    public List<NotificationDetail> userNotifications(Authentication authentication) {
        return notificationService
                .getUserNotifications((User) authentication.getPrincipal())
                .stream().map(NotificationDetail::new).toList();
    }

    @DeleteMapping("/{id}")
    public void dismissNotification(@PathVariable String id) {
        notificationService.dismissNotification(id);
    }
}
