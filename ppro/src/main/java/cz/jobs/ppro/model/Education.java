package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="educations")
@Getter
@Setter
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cv_id", nullable = false)
    private CV cv;

    @Column(nullable = true)
    private String education;
    private int startYear;
    private int endYear;

}
