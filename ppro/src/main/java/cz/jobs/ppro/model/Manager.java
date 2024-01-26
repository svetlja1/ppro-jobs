package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="managers")
@NoArgsConstructor
@Getter
@Setter
public class Manager{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = true)
    private String company;

    public Manager(User user, String company) {
        this.user = user;
        this.company = company;
    }
}
