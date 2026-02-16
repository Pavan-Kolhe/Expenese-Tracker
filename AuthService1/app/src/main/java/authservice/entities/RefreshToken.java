package authservice.entities;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  String id;

    private  String token;
    private Instant expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName =  "user_id")
    private UserInfo userInfo;


}
// below is th is fix i think
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@Table(name = "tokens")
//public class RefreshToken {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String token;
//
//    private Instant expiryDate;
//
//    @OneToOne
//    @JoinColumn(name = "id", referencedColumnName = "user_id")
//    private UserInfo userInfo;
//}
