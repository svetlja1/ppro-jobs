package cz.jobs.ppro.controller;

import cz.jobs.ppro.components.AuthenticationFacade;
import cz.jobs.ppro.exception.JobNotFoundException;
import cz.jobs.ppro.model.*;
import cz.jobs.ppro.service.JobService;
import cz.jobs.ppro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class JobsController {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private JobService jobService;
    @Autowired
    private UserService userService;
    @GetMapping("/add_job")
    public String addJob(Model model) {
        Manager manager = userService.findManagerByUserId(authenticationFacade.getAuthenticatedUserId());
        model.addAttribute("manager", manager);
        return "add_job";
    }
    @PostMapping("/add_job")
    public String postJob(@ModelAttribute("job") @Valid Job job, BindingResult result) {
        if (result.hasErrors()) {
            return "add_job";
        }

        Long userId = authenticationFacade.getAuthenticatedUserId();

        job.setUserId(userId);
        jobService.addJob(job);
        return "redirect:/dashboard?jobPostSuccess";
    }

    @GetMapping("/my_jobs")
    public String myJobs(Model model) {
        Optional<List<Job>> jobsOptional = jobService.getJobsByUserId(authenticationFacade.getAuthenticatedUserId());
        if(jobsOptional.isPresent()) {
            List<Job> jobs = jobsOptional.get();
            model.addAttribute("jobs", jobs);
        }
        return "my_jobs";
    }

    @GetMapping("/all_jobs")
    public String allJobs(@RequestParam(name = "category", required = false) String category,
                          @RequestParam(name = "area", required = false) String area,
                          Model model) {
        List<Job> jobs;

        if (category != null && !category.isEmpty() && area != null && !area.isEmpty()) {
            // Fetch jobs based on both category and area
            Optional<List<Job>> optionalJob = jobService.getJobsByCategoryAndArea(category, area);
            if (optionalJob.isPresent()) {
                jobs = optionalJob.get();
            } else {
                throw new JobNotFoundException("Jobs cannot be found for the given category and area");
            }
        } else if (category != null && !category.isEmpty()) {
            // Fetch jobs based on category
            Optional<List<Job>> optionalJob = jobService.getJobsByCategory(category);
            if (optionalJob.isPresent()) {
                jobs = optionalJob.get();
            } else {
                throw new JobNotFoundException("Jobs cannot be found for the given category");
            }
        } else if (area != null && !area.isEmpty()) {
            // Fetch jobs based on area
            Optional<List<Job>> optionalJob = jobService.getJobsByArea(area);
            if (optionalJob.isPresent()) {
                jobs = optionalJob.get();
            } else {
                throw new JobNotFoundException("Jobs cannot be found for the given area");
            }
        } else {
            // Fetch all jobs if no category or area is specified
            jobs = jobService.getAllJobs();
        }

        model.addAttribute("jobs", jobs);
        return "all_jobs";
    }


    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        User user = userService.findUserById(authenticationFacade.getAuthenticatedUserId());

        // Delete only if
        if(job.getUserId() == authenticationFacade.getAuthenticatedUserId() || (user.getRole().equals("ADMIN"))) {
            jobService.deleteJob(jobId);
            return ResponseEntity.ok("Job deleted successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot delete job of other manager");
        }

    }

    @GetMapping("/jobs/{jobId}")
    public String getJob(@PathVariable Long jobId, Model model) {
        Job job = jobService.getJobById(jobId);
        Long userId = authenticationFacade.getAuthenticatedUserId();
        if(job != null) {
            model.addAttribute("job", job);
            model.addAttribute("userId", userId);
        }
        return "job";
    }

    @PostMapping("/jobs/{jobId}/reply")
    public String replyHandle(@PathVariable Long jobId, @ModelAttribute("reply") @Valid Reply reply) {
        reply.setJob(jobService.getJobById(jobId));
        Seeker seeker = userService.findSeekerByUserId(authenticationFacade.getAuthenticatedUserId());
        reply.setSeeker(seeker);
        reply.setCvUrl(seeker.getCv().getLatestCvUrl());
        jobService.addReply(reply);
        return "redirect:/jobs/{jobId}?replySent";
    }
}
