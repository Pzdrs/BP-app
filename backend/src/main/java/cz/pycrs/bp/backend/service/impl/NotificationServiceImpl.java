package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.repository.NotificationRepository;
import cz.pycrs.bp.backend.service.NotificationService;
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

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findAllByUser(user);
    }

    @Override
    public void addNotificationEmittor(Authentication authentication, SseEmitter emitter) {
        User user = (User) authentication.getPrincipal();

        userEmitters.put(user.getId().toString(), emitter);
        emitter.onCompletion(() -> userEmitters.remove(user.getId().toString()));
        emitter.onTimeout(() -> userEmitters.remove(user.getId().toString()));
    }

    @Override
    public void sendNotification(User user, Notification notification) throws IOException {
        notification.setUser(user);
        notificationRepository.save(notification);
        userEmitters.get(user.getId().toString()).send(notification);
    }

    @Override
    public void sendNotifications(User user, Notification... notification) throws IOException {
        for (Notification n : notification) {
            sendNotification(user, n);
        }
    }
}
