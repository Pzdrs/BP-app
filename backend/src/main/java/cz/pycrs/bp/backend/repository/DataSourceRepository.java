package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.DataSource;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DataSourceRepository extends MongoRepository<DataSource, String> {
    Optional<DataSource> findByMac(String mac);
}
