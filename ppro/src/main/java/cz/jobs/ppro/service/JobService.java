package cz.jobs.ppro.service;

import cz.jobs.ppro.model.Job;
import cz.jobs.ppro.model.Reply;
import cz.jobs.ppro.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface JobService {
    void addJob(Job job);
    Optional<List<Job>> getJobsByUserId(Long userId);

    Optional<List<Job>> getJobsByCategory(String category);

    Optional<List<Job>> getJobsByArea(String category);

    Optional<List<Job>> getJobsByCategoryAndArea(String category, String area);

    List<Job> getAllJobs();

    void deleteJob(long jobId);

    Job getJobById(long jobId);
    void addReply(Reply reply);
}
