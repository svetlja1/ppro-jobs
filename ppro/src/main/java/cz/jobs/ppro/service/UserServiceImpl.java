package cz.jobs.ppro.service;

import cz.jobs.ppro.exception.UserAlreadyExistsException;
import cz.jobs.ppro.exception.UserNotFoundException;
import cz.jobs.ppro.model.Manager;
import cz.jobs.ppro.model.User;
import cz.jobs.ppro.repository.ManagerRepository;
import cz.jobs.ppro.repository.UserRepository;
import cz.jobs.ppro.security.CustomSimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void registerUser(User user) {

        String username = user.getUsername();
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with the username '%s' already exists.", username));
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        if(user.getRole().equals("MANAGER")) {
            User newUser = new User(user.getUsername(), hashedPassword, user.getRole());
            userRepository.save(newUser);

            Manager newManager = new Manager(newUser, null);
            managerRepository.save(newManager);
        }
//        else if(user.getRole().equals("SEEKER")) {
//            User newUser = new User(user.getUsername(), hashedPassword, user.getRole());
//        }


    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Uživatel s uživatelským jménem " + username + " nenalezen");
        } else {
            Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + userOptional.get().getRole()));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(userOptional.get().getUsername())
                    .password(userOptional.get().getPassword())
                    .authorities(authorities)
                    .build();
        }
    }

    @Override
    public Long findIdByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new UserNotFoundException("User with username " + username + " not found");
        }

    }
}
