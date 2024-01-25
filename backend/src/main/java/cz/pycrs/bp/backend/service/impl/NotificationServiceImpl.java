package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.repository.NotificationRepository;
import cz.pycrs.bp.backend.repository.UserRepository;
import cz.pycrs.bp.backend.service.NotificationService;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, SseEmitter> userEmitters = new HashMap<>();

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findAllByUser(user);
    }

    @Override
    public SseEmitter createEmitter(User user) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(user.getId().toString(), emitter);
        emitter.onCompletion(() -> userEmitters.remove(user.getId().toString()));
        emitter.onTimeout(() -> userEmitters.remove(user.getId().toString()));
        return emitter;
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        notification.setUser(user);
        notification = notificationRepository.save(notification);
        try {
            SseEmitter sseEmitter = userEmitters.get(user.getId().toString());
            if (sseEmitter != null) sseEmitter.send(notification);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to send notification to user %s", user.getId()), e);
        }
    }

    @Override
    public void sendNotifications(User user, Notification... notification) {
        for (Notification n : notification) {
            sendNotification(user, n);
        }
    }

    @Override
    public void notifyAdministrators(Notification... notifications) {
        userService.getAllAdministrators().forEach(user -> sendNotifications(user, notifications));
    }

    @Override
    public void dismissNotification(String id) {
        notificationRepository.deleteById(id);
    }
}
