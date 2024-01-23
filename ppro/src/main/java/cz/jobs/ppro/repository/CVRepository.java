package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVRepository extends JpaRepository<CV, Long> {
}