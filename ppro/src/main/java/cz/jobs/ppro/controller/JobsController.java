package cz.jobs.ppro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobsController {

    @GetMapping("/add_job")
    public String welcome() {
        return "add_job";
    }
}
