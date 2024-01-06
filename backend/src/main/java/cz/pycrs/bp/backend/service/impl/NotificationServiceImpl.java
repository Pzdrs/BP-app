package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.repository.NotificationRepository;
import cz.pycrs.bp.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findAllByUser(user);
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        notification.setUser(user);
        notificationRepository.save(notification);
        //sse
    }

    @Override
    public void sendNotifications(User user, Notification... notification) {
        for (Notification n : notification) {
            sendNotification(user, n);
        }
    }
}
