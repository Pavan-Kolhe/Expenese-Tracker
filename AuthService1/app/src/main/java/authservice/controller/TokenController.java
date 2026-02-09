package authservice.controller;

import authservice.entities.RefreshToken;
import authservice.request.AuthRequestDTO;
import authservice.request.RefreshTokenRequestDTO;
import authservice.response.JwtResponseDTO;
import authservice.service.JwtService;
import authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController
{

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    public TokenController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }


    //    @PostMapping("auth/v1/login")
//    public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
//        if(authentication.isAuthenticated()){
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
//            return new ResponseEntity<>(JwtResponseDTO.builder()
//                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
//                    .token(refreshToken.getToken())
//                    .build(), HttpStatus.OK);
//
//        } else {
//            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
@PostMapping("auth/v1/login")
public ResponseEntity AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
    try {
        // 1. Attempt authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword())
        );

        // 2. If we reach here, authentication was successful
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return new ResponseEntity<>(JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);
        } else {
            // This block is rarely reached in standard Spring Security
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);
        }

    } catch (UsernameNotFoundException e) {
        // 3. Handle invalid username
        return new ResponseEntity<>("Invalid user request", HttpStatus.BAD_REQUEST);
    } catch (BadCredentialsException e) {
        // 4. Handle invalid password
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        // 5. Handle generic errors
        return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                // Returns: Optional<RefreshToken>

                .map(refreshTokenService::verifyExpiration)
                // If token exists:
                //   - Calls: refreshTokenService.verifyExpiration(token)
                //   - Checks if expired, throws exception if yes
                //   - Returns: Optional<RefreshToken> (still wrapped)

                .map(RefreshToken::getUserInfo)
                // If token valid:
                //   - Calls: token.getUserInfo()
                //   - Returns: Optional<UserInfo>

                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken())
                            .build();
                })
                // If userInfo exists:
                //   - Generates new JWT
                //   - Builds response DTO
                //   - Returns: Optional<JwtResponseDTO>

                .orElseThrow(() -> new RuntimeException("Refresh Token is not in DB..!!"));
        // If any step returns empty Optional:
        //   - Throws exception
    }
// easy code of the above
//    public JwtResponseDTO refreshToken(RefreshTokenRequestDTO dto) {
//        // Step 1: Find token in database
//        Optional<RefreshToken> optionalToken = refreshTokenService.findByToken(dto.getToken());
//
//        if (!optionalToken.isPresent()) {
//            throw new RuntimeException("Refresh Token is not in DB..!!");
//        }
//
//        RefreshToken refreshToken = optionalToken.get();
//
//        // Step 2: Verify expiration
//        RefreshToken validToken = refreshTokenService.verifyExpiration(refreshToken);
//
//        // Step 3: Get user info
//        UserInfo userInfo = validToken.getUserInfo();
//
//        // Step 4: Generate new access token
//        String accessToken = jwtService.GenerateToken(userInfo.getUsername());
//
//        // Step 5: Return response
//        return JwtResponseDTO.builder()
//                .accessToken(accessToken)
//                .token(dto.getToken())
//                .build();
//    }

}