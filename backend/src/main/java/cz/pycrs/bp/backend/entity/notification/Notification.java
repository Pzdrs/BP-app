package cz.pycrs.bp.backend.entity.notification;

import cz.pycrs.bp.backend.entity.user.User;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Document("notifications")
@Data
public class Notification {
    public enum Severity {
        INFO, WARNING, ERROR
    }

    @MongoId
    private ObjectId id;
    @DocumentReference
    private User user;
    private Severity severity;


    private String title, message;
    private Map<String, Object> metadata;

    @CreatedDate
    private Date created;

    protected Notification() {
    }

    public Notification(Severity severity, String title, String message, Map<String, Object> metadata) {
        this.severity = severity;
        this.title = title;
        this.message = message;
        this.metadata = metadata;
    }

    public Notification(Severity severity, String title, String message) {
        this.severity = severity;
        this.title = title;
        this.message = message;
    }
}
