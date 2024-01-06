package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.notification.dto.NotificationDetail;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDetail> userNotifications(Authentication authentication) {
        return notificationService
                .getUserNotifications((User) authentication.getPrincipal())
                .stream().map(NotificationDetail::new).toList();
    }
}
