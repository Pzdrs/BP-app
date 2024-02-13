package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.datapoint.DataPoint;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DataPointRepository extends MongoRepository<DataPoint, String> {
    List<DataPoint> findAllBySource(DataSource dataSource);
}
