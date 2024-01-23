package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
}