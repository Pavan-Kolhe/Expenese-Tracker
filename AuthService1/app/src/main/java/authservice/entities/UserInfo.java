package authservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)// Instantly when the user is loaded from the DB (because of FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole>roles = new HashSet<>();   // all teh user roles will be stored into this set m:n relation


//    @ManyToMany and @JoinTable, Hibernate automatically creates a third table in your database called users_roles
//    when the application starts (if you have spring.jpa.hibernate.ddl-auto=update).
//    You do NOT see a Java Class for this table, but the table physically exists in the database

}


// notes:

//Java vs. JSON: In your code, it is a Set (unique objects), but it will always appear as an Array [] in JSON because JSON has no "Set" type.
//The Hidden Table: The @ManyToMany annotation automatically creates a physical third table (users_roles) in your database to link User IDs to Role IDs.
//Standard Structure: This 3-table setup (Users, Roles, Join Table) is the mandatory industry standard for databases, regardless of whether you have 10 or 10 million users.
//When to Customize: Keep your current @ManyToMany code for simple links; only create a manual middle class if you need extra data (like assigned_date) on the relationship itself.
//Production Tip: The structure is correct, but in production, disable auto-creation (ddl-auto) and write the SQL manually to prevent accidental data loss.