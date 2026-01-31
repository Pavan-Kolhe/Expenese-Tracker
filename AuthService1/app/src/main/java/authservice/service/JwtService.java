package authservice.service;
// JWT library - handles token creation/parsing
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;      // Base64 decoding for secret key
import io.jsonwebtoken.security.Keys;    // Key generation utilities

// Spring Security
import org.springframework.security.core.userdetails.UserDetails; // User info

// Spring Core
import org.springframework.stereotype.Service;  // Marks as service layer
import org.springframework.beans.factory.annotation.Value; // Inject properties

// Java utilities
import java.security.Key;                // Cryptographic key interface
import java.util.Date;                   // Token expiration dates
import java.util.HashMap;                // Store custom claims
import java.util.Map;
import java.util.function.Function;      // Functional interface for extracting claims


@Service  // Spring bean - can be @Autowired in other classes
//Why @Service? Marks this as a business logic layer component ,Spring creates a singleton instance ,Can be injected into controllers/other services
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long EXPIRATION;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  //username (subject)
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired", e);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())
                    && !isTokenExpired(token));

        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: {}"+ e.getMessage());
            return false;
        } catch (JwtException e) {
            System.out.println("Invalid token: {}"+ e.getMessage());
            return false;
        }
    }

    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }



    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
