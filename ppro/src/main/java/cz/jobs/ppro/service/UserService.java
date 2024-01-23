package cz.jobs.ppro.service;

import cz.jobs.ppro.model.CV;
import cz.jobs.ppro.model.Manager;
import cz.jobs.ppro.model.Seeker;
import cz.jobs.ppro.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    void registerUser(User user);

    UserDetails loadUserByUsername(String username);

    Long findIdByUsername(String username);

    User findUserById(Long id);

    Seeker findSeekerByUserId(Long id);
    Manager findManagerById(Long id);
    void updateCV(CV cv);
}
