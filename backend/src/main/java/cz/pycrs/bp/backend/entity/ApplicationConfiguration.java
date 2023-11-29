package cz.pycrs.bp.backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("configuration")
@Data
public class ApplicationConfiguration {
    @Id
    public final String key;
    public final Object value;
}
