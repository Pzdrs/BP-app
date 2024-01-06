package cz.pycrs.bp.backend.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("datasource")
@Data
public class DataSource {
    @MongoId
    private ObjectId id;
    private String mac;
    private String name = null;
    private boolean awaitingAdoption = true;

    public DataSource(String mac) {
        this.mac = mac;
    }
}
