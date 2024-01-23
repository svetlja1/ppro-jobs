package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EducationRepository extends JpaRepository<Education, Long> {
}