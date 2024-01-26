package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
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
//nebo cvs?
    @OneToMany(mappedBy="cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educationList = new ArrayList<>();

    @OneToMany(mappedBy="cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingExperience> workingExperienceList = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "education_id", referencedColumnName = "id")
//    private WorkingExperience workingExperience;
//
    @Column(nullable = true)
    private String skills;

    @Column(nullable = true)
    private String latestCvUrl;
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
