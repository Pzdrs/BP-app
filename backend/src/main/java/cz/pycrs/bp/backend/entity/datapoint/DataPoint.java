package cz.pycrs.bp.backend.entity.datapoint;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document("datapoints")
@Data
@NoArgsConstructor
public class DataPoint {
    @MongoId
    private ObjectId id;
    @DocumentReference
    private DataSource source;
    private double lng, lat, alt;
    private double speed, course;

    @CreatedDate
    private Date timestamp;

    public DataPoint(DataSource dataSource, double longitude, double latitude, double altitude, double speed, double course) {
        this.source = dataSource;
        this.lng = longitude;
        this.lat = latitude;
        this.alt = altitude;
        this.speed = speed;
        this.course = course;
    }
}
