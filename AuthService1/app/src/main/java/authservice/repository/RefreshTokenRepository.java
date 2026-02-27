package authservice.repository;


import authservice.entities.RefreshToken;
import authservice.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Integer> {

    Optional<RefreshToken> findByToken(String token);
    // do SQL automatically JPA
    Optional<RefreshToken> findByUserInfo(UserInfo user);
}
