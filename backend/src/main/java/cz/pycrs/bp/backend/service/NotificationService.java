package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(User user);

    void addNotificationEmittor(Authentication authentication, SseEmitter emitter);

    void sendNotification(User user, Notification notification) throws IOException;
    void sendNotifications(User user, Notification ...notification) throws IOException;
}
