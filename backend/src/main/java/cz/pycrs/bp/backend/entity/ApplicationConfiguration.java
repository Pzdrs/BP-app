package cz.pycrs.bp.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("configuration")
@Data
@AllArgsConstructor
public class ApplicationConfiguration {
    @Id
    private String key;
    private Object value;
}
