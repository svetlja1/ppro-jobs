package cz.jobs.ppro.controller;

import cz.jobs.ppro.components.AuthenticationFacade;
import cz.jobs.ppro.functions.PdfGenerator;
import cz.jobs.ppro.model.*;
import cz.jobs.ppro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationFacade authenticationFacade;


    @GetMapping("/dashboard")
    public String welcome() {
        return "dashboard";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        //model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> performLogin(@ModelAttribute("user") @Valid User user) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            );

            Authentication authenticated = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authenticated);

            return ResponseEntity.ok("Přihlášení úspěšné");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Přihlášení selhalo");
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Long userId = authenticationFacade.getAuthenticatedUserId();
        User user = userService.findUserById(userId);
        if(user.getRole().equals("SEEKER")) {
            Seeker seeker = userService.findSeekerByUserId(userId);
            model.addAttribute("seeker", seeker);
        } else if(user.getRole().equals("MANAGER")) {
            Manager manager = userService.findManagerByUserId(userId);
            model.addAttribute("manager", manager);
        }
        return "profile";
    }

    @PutMapping("/profile")
    public String editProfile(@ModelAttribute Seeker seeker, @ModelAttribute Manager manager) {
        Long userId = authenticationFacade.getAuthenticatedUserId();
        User user = userService.findUserById(userId);
        if(user.getRole().equals("SEEKER")) {
            CV cv = seeker.getCv();
            PersonalData personalData = cv.getPersonalData();

            // Update CV of authenticated Seeker
            Seeker existingSeeker = userService.findSeekerByUserId(userId);
            CV existingCV = existingSeeker.getCv();
            PersonalData existingCVPersonalData = existingCV.getPersonalData();

            existingCVPersonalData.setDateOfBirth(personalData.getDateOfBirth());
            existingCVPersonalData.setPhoneNumber(personalData.getPhoneNumber());
            existingCVPersonalData.setEmail(personalData.getEmail());
            existingCVPersonalData.setFullName(personalData.getFullName());
            existingCVPersonalData.setResidence(personalData.getResidence());

            existingCV.setSkills(cv.getSkills());

            userService.savePersonalData(existingCVPersonalData);

            List<Education> educationList = seeker.getCv().getEducationList();

            userService.deleteEducationsByCvId(existingCV.getId());

            // Save each education to the database
            for (Education education : educationList) {
                // Set the CV for each education
                education.setCv(existingCV);
                userService.saveEducation(education);
            }


            List<WorkingExperience> workingExperienceList = seeker.getCv().getWorkingExperienceList();

            userService.deleteExperiencesByCvId(existingCV.getId());

            // Save each education to the database
            for (WorkingExperience workingExperience : workingExperienceList) {
                // Set the CV for each education
                workingExperience.setCv(existingCV);
                userService.saveExperience(workingExperience);
            }

            long actualTime = System.currentTimeMillis();
            String pdfFilePath = existingCV.getId() + "_" + actualTime + ".pdf";
            PdfGenerator.generatePdf(cv, pdfFilePath);
            existingCV.setLatestCvUrl(pdfFilePath);

            userService.saveCV(existingCV);

            existingSeeker.setCv(existingCV);
            userService.saveSeeker(existingSeeker);
        }

        if(user.getRole().equals("MANAGER")){
            Manager existingManager = userService.findManagerByUserId(userId);
            existingManager.setCompany(manager.getCompany());
            userService.saveManager(existingManager);
        }
        return "redirect:/profile";
    }


    @GetMapping("/cv")
    @ResponseBody
    public ResponseEntity<Resource> downloadCv() throws IOException {
        // Retrieve the CV data (replace 1 with the actual CV ID or use another method to get CV data)
        Long userId = authenticationFacade.getAuthenticatedUserId();
        Seeker seeker = userService.findSeekerByUserId(userId);
        CV cv = userService.getCvById(seeker.getCv().getId());

        // Generate the PDF
        String pdfFilePath = cv.getId()+".pdf";
        PdfGenerator.generatePdf(cv, pdfFilePath);

        // Create a Resource from the generated PDF file
        Resource resource = new FileSystemResource(new File(pdfFilePath));

        // Set headers for the download response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cv.pdf");

        try {
            // Copy the file to a temporary location
            Path tempFile = Files.createTempFile("cv", ".pdf");
            Files.copy(resource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            resource = new FileSystemResource(tempFile.toFile());
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/download-cv/{cvPath}.pdf")
    public ResponseEntity<Resource> downloadCV(@PathVariable String cvPath) throws IOException {
        // Retrieve CV file information from the service
        // Create a Resource object for the file
        Resource resource = new FileSystemResource(cvPath + ".pdf");

        // Set up the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "cv.pdf");

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
