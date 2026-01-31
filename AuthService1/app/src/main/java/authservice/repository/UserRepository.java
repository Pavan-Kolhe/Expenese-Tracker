package authservice.repository;


import authservice.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo,Long> {
    // error: it should be <UserInfo,String >

    public UserInfo findByUsername(String username);

    //pring Data JPA uses a strategy called "Query Derivation".
    // It breaks your method name into three parts:
    // Method Part     Meaning  to Spring                            Resulting SQL Part
    //    find        "I need to SELECT data.                       "SELECT *
    //    By          "Here comes the condition (WHERE clause).     "WHERE
    //    Username     "Match this specific column.                 "username = ?
}
