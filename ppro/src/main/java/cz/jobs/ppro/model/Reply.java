package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="replies")
@Getter
@Setter
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name="seeker_id", nullable = false)
    private Seeker seeker;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String cvUrl;


}
