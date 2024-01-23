package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="cvs")
@Getter
@Setter
@NoArgsConstructor
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "personal_data_id", referencedColumnName = "id")
    private PersonalData personalData;

    @OneToMany(mappedBy="cv")
    private Set<Education> educationList;
//
//    @OneToMany
//    @JoinColumn(name = "education_id", referencedColumnName = "id")
//    private WorkingExperience workingExperience;
//
    @Column(nullable = true)
    private String skills;
//
//

    public CV(PersonalData personalData) {
        this.personalData = personalData;
    }


//    public CV(Seeker seeker, PersonalData personalData, Education education, WorkingExperience workingExperience, String skills) {
//        this.seeker = seeker;
//        this.personalData = personalData;
//        this.education = education;
//        this.workingExperience = workingExperience;
//        this.skills = skills;
//    }
}
