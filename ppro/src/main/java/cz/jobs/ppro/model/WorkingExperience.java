package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="working_experiences")
@Getter
@Setter
@NoArgsConstructor
public class WorkingExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cv_id", nullable = false)
    private CV cv;

    @Column(nullable = true)
    private String workingExperience;
    private int startYear;
    private int endYear;

    public WorkingExperience(CV cv, String workingExperience, int startYear, int endYear) {
        this.cv = cv;
        this.workingExperience = workingExperience;
        this.startYear = startYear;
        this.endYear = endYear;
    }
}
