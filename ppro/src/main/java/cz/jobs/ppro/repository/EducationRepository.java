package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Education;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface EducationRepository extends JpaRepository<Education, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Education e WHERE e.cv.id = :cvId")
    void deleteByCvId(Long cvId);
}