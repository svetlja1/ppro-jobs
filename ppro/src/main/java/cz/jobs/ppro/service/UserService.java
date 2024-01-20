package cz.jobs.ppro.service;

import cz.jobs.ppro.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    void registerUser(User user);

    UserDetails loadUserByUsername(String username);
}
