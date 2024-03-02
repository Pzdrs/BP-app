package cz.pycrs.bp.backend.repository;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {
    Optional<AccessToken> findByValue(String value);
}
