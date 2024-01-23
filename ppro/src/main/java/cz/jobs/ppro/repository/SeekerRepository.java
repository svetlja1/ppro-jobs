package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Seeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeekerRepository extends JpaRepository<Seeker, Long> {
    Seeker findByUserId(Long id);
}
