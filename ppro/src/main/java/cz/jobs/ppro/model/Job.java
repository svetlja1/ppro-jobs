package cz.jobs.ppro.model;

import cz.jobs.ppro.security.CustomSimpleGrantedAuthority;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobName;

    @Column(nullable = false)
    private String jobCategory;

    @Column(nullable = false)
    private String jobDescription;

    @Column(nullable = false)
    private int jobSalary;

    @Column(nullable = false)
    private String jobCompany;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String jobArea;

    @OneToMany(mappedBy="job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    public Job(String jobName, String jobCategory, String jobDescription, int jobSalary, String jobCompany, long userId, String jobArea) {
        this.jobName = jobName;
        this.jobCategory = jobCategory;
        this.jobDescription = jobDescription;
        this.jobSalary = jobSalary;
        this.jobCompany = jobCompany;
        this.userId = userId;
        this.jobArea = jobArea;
    }
}
