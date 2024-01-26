package cz.jobs.ppro.repository;

import cz.jobs.ppro.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}