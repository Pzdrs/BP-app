package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.notification.Notification;
import cz.pycrs.bp.backend.entity.user.User;

import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(User user);

    void sendNotification(User user, Notification notification);
    void sendNotifications(User user, Notification ...notification);
}
