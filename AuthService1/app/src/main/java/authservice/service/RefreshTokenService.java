package authservice.service;


import authservice.entities.RefreshToken;
import authservice.entities.UserInfo;
import authservice.repository.RefreshTokenRepository;
import authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//    @Autowired
//    UserRepository userRepository;

    // the above thing is happening with a constructor  recommended for production
//    You are actually using @Autowired, but it is happening implicitly (automatically) via the Constructor.

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Value("${REFRESH_TOKEN_EXPIRY}")
    private Long refreshTokenExpiry;


    public RefreshToken createRefreshToken(String username){
        UserInfo  userInfoExtracted = userRepository.findByUsername(username);
        RefreshToken refreshToken = refreshTokenRepository.findByUserInfo(userInfoExtracted)
                .orElse(new RefreshToken());

        // 2. Update the fields (Hibernate will cleverly UPDATE if it existed, or INSERT if new)
        refreshToken.setUserInfo(userInfoExtracted);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiry));

        // 3. Save to database
        return refreshTokenRepository.save(refreshToken);

//        RefreshToken refreshToken = RefreshToken.builder()
//                .userInfo(userInfoExtracted)
//                .token(UUID.randomUUID().toString())
//                .expiryDate(Instant.now().plusMillis(refreshTokenExpiry))
//                .build();
//
//        return refreshTokenRepository.save(refreshToken);

        /*
       above one is same as
       return refreshTokenRepositoty.save( new RefreshToken(   //  public RefreshToken(String token, Instant expiryDate, UserInfo userInfo)
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenExpiry),
                userInfoExtracted
        ));
       */
    }


    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())< 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "RefreshToken expired. Please make a new login  .. !");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token){
            return  refreshTokenRepository.findByToken(token);
    }
}
