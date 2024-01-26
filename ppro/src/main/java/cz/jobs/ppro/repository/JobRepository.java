package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Job;
import cz.jobs.ppro.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<List<Job>> findAllByUserId(Long userId);
    Optional<List<Job>> findAllByJobCategory(String userId);

    Optional<List<Job>> findAllByJobArea(String area);

    Optional<List<Job>> findAllByJobCategoryAndJobArea(String category, String area);


}
