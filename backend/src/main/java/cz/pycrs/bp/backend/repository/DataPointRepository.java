package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.DataPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataPointRepository extends MongoRepository<DataPoint, String> {
}
