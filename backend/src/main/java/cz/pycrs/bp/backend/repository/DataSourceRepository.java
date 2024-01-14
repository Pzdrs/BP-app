package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.datasource.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DataSourceRepository extends MongoRepository<DataSource, String> {
    Optional<DataSource> findByMac(String mac);

    List<DataSource> findAllByGroupsIsNotEmpty();
}
