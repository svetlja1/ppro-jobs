package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.WorkingExperience;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkingExperienceRepository extends JpaRepository<WorkingExperience, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM WorkingExperience e WHERE e.cv.id = :cvId")
    void deleteByCvId(Long cvId);
}