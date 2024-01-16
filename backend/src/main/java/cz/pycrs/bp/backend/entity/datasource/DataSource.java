package cz.pycrs.bp.backend.entity.datasource;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("datasource")
@Data
@NoArgsConstructor
public class DataSource {
    @MongoId
    private ObjectId id;
    private String mac;
    private String name;
    private String color;
    private boolean adopted = false;
    private List<String> groups = new ArrayList<>();

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    public DataSource(String mac) {
        this.mac = mac;
    }
}
