package cz.pycrs.bp.backend.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document("datapoint")
@Data
public class DataPoint {
    @MongoId
    private ObjectId id;
    @DocumentReference
    private DataSource dataSource;
    private double longitude, latitude, altitude;
    private double speed, course;

    @CreatedDate
    private Date created;

    public DataPoint(DataSource dataSource, double longitude, double latitude, double altitude, double speed, double course) {
        this.dataSource = dataSource;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.speed = speed;
        this.course = course;
    }
}
