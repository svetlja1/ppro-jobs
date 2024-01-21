package cz.jobs.ppro.model;

import cz.jobs.ppro.security.CustomSimpleGrantedAuthority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    public User(String username, String hashedPassword, String role) {
        this.username = username;
        this.password = hashedPassword;
        this.role = role;
    }
}
