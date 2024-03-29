package cz.jobs.ppro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="seekers")
@NoArgsConstructor
@Getter
@Setter
public class Seeker{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private CV cv;

    @OneToMany(mappedBy="seeker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    public Seeker(User user, CV cv) {
        this.user = user;
        this.cv = cv;
    }
}
