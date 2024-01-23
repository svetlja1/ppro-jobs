package cz.jobs.ppro.controller;

import cz.jobs.ppro.components.AuthenticationFacade;
import cz.jobs.ppro.model.CV;
import cz.jobs.ppro.model.Manager;
import cz.jobs.ppro.model.Seeker;
import cz.jobs.ppro.model.User;
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
            Manager manager = userService.findManagerById(userId);
            model.addAttribute("manager", manager);
        }
        return "profile";
    }

    @PutMapping("/profile")
    public String editProfile(@ModelAttribute Seeker seeker) {
        CV cv = seeker.getCv();

        Long userId = authenticationFacade.getAuthenticatedUserId();
        Seeker seeker1 = userService.findSeekerByUserId(userId);


        cv.setId(seeker1.getCv().getId());

        // TODO nejdriv uloz personal data atd..
        userService.updateCV(cv);
        return "redirect:/profile";
    }
}
