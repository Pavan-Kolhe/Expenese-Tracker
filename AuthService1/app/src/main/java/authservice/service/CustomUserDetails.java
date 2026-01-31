package authservice.service;

import authservice.entities.UserInfo;
import authservice.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {

    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role : byUsername.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    // Spring Security needs a CONTRACT
// Every security component expects GrantedAuthority
//    @PreAuthorize("hasRole('ADMIN')")  // Works because of GrantedAuthority
//    @Secured("ROLE_USER")              // Works because of GrantedAuthority
    // so we done this object instead of a String List

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}








//   NOTES::
//
//
// HTTP Request → Controller → Service → Repository → Database
//                    ↓           ↓          ↓
//                  DTO      Business    Entity
//                           Logic
//
// Controller → HTTP only
//
//Service → business logic
//
//Repository → DB only
//
//Entity → table mapping
//
//DTO → API contract
//
//Exception → centralized
//
//Same architecture as Express, just more enforced
//
//
// MERN and SB
//┌─────────────────────┬──────────────────────────┬─────────────────────┐
//        │   Spring Boot       │      Express.js          │      Purpose        │
//        ├─────────────────────┼──────────────────────────┼─────────────────────┤
//        │                     │                          │                     │
//        │  @RestController    │  app.get/post/put/delete │  Route handlers     │
//        │         │           │           │              │         │           │
//        │         ▼           │           ▼              │         ▼           │
//        │                     │                          │                     │
//        │    @Service         │  Middleware/Services     │  Business logic     │
//        │         │           │           │              │         │           │
//        │         ▼           │           ▼              │         ▼           │
//        │                     │                          │                     │
//        │   @Repository       │  Database models/queries │  Data access        │
//        │         │           │           │              │         │           │
//        │         ▼           │           ▼              │         ▼           │
//        │                     │                          │                     │
//        │     @Entity         │  Mongoose/Sequelize      │  Database models    │
//        │                     │                          │                     │
//        ├─────────────────────┼──────────────────────────┼─────────────────────┤
//        │                     │                          │                     │
//        │       DTO           │  Request/Response objects│  Data validation    │
//        │                     │                          │                     │
//        │   @Autowired        │  require() / imports     │  Dependency inject  │
//        │                     │                          │                     │
//        │ application.        │      .env / config       │  Configuration      │
//        │  properties         │                          │                     │
//        │                     │                          │                     │
//        │ @Transactional      │  DB transactions         │  Transaction mgmt   │
//        │                     │                          │                     │
//        └─────────────────────┴──────────────────────────┴─────────────────────┘
//
//HTTP Request
//          │
//          ▼
//    ┌──────────────┐
//    │ @Controller  │ ◄──── Handles HTTP, validates input
//    │  (Routes)    │
//    └──────┬───────┘
//           │
//           │ uses DTO (Request)
//           │
//           ▼
//    ┌──────────────┐
//    │  @Service    │ ◄──── Business logic, transactions
//    │ (Logic Layer)│
//    └──────┬───────┘
//           │
//           │ uses Entity
//           │
//           ▼
//    ┌──────────────┐
//    │ @Repository  │ ◄──── Database queries (CRUD)
//    │ (Data Layer) │
//    └──────┬───────┘
//           │
//           ▼
//    ┌──────────────┐
//    │   Database   │ ◄──── Actual data storage
//    │   (MySQL/    │
//    │   Postgres)  │
//    └──────────────┘
//           │
//           │ returns Entity
//           │
//           ▼
//    [ Service processes ]
//           │
//           │ converts to DTO (Response)
//           │
//           ▼
//    [ Controller returns JSON ]
//           │
//           ▼
//     HTTP Response