package cz.jobs.ppro.model;

import cz.jobs.ppro.security.CustomSimpleGrantedAuthority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "personal_data")
@Getter
@Setter
@NoArgsConstructor
public class PersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = true)
    private String residence;

    @Column(nullable = true)
    private Date dateOfBirth;

    @Column(nullable = true)
    private int phoneNumber;

    @Column(nullable = true)
    private String email;

    public PersonalData(String fullName, String residence, Date dateOfBirth, int phoneNumber, String email) {
        this.fullName = fullName;
        this.residence = residence;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
