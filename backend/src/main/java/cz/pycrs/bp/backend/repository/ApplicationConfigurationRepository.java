package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.ApplicationConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationConfigurationRepository extends MongoRepository<ApplicationConfiguration, String> {
}
