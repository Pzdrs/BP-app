package cz.pycrs.bp.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("configurations")
@Data
@AllArgsConstructor
public class ApplicationConfiguration {
    @Id
    String key;
    Object value;
}
