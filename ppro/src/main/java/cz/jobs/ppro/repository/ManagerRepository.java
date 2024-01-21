package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
