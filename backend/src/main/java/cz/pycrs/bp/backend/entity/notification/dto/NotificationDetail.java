package cz.pycrs.bp.backend.entity.notification.dto;

import cz.pycrs.bp.backend.entity.notification.Notification;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public record NotificationDetail(
        String id,
        Notification.Severity severity,
        String title,
        String message,
        Map<String, Object> metadata,
        Date created
) {
    public NotificationDetail(Notification notification) {
        this(
                notification.getId().toString(),
                notification.getSeverity(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getMetadata(),
                notification.getCreated()
        );
    }
}
