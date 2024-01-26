package cz.jobs.ppro.service;

import cz.jobs.ppro.model.Job;
import cz.jobs.ppro.model.Reply;
import cz.jobs.ppro.repository.JobRepository;
import cz.jobs.ppro.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService{

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public void addJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Optional<List<Job>> getJobsByUserId(Long userId) {
        return jobRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteJob(long jobId) {
        jobRepository.deleteById(jobId);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<List<Job>> getJobsByCategory(String category) {
        return jobRepository.findAllByJobCategory(category);
    }

    @Override
    public Optional<List<Job>> getJobsByArea(String area) {
        return jobRepository.findAllByJobArea(area);
    }

    @Override
    public Optional<List<Job>> getJobsByCategoryAndArea(String category, String area) {
        return jobRepository.findAllByJobCategoryAndJobArea(category, area);
    }

    @Override
    public Job getJobById(long jobId){
            Optional<Job> job = jobRepository.findById(jobId);
        return job.orElse(null);
    }

    @Override
    public void addReply(Reply reply){
        replyRepository.save(reply);
    }
}
