package cz.pycrs.bp.backend.entity.accesstoken;

import cz.pycrs.bp.backend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.token.Token;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Document("accesstokens")
@Data
@AllArgsConstructor
@Builder
public class AccessToken implements Token {
    @Id
    private final String token;
    @DocumentReference
    private final User user;
    private final LocalDateTime created;
    private String description;
    private boolean disabled;
    private LocalDateTime expiry;

    public boolean isExpired() {
        return this.expiry != null && expiry.isAfter(LocalDateTime.now());
    }

    @Override
    public String getKey() {
        return this.token;
    }

    @Override
    public long getKeyCreationTime() {
        return this.created.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public String getExtendedInformation() {
        return null;
    }
}
