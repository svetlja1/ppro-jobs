package cz.jobs.ppro.service;

import cz.jobs.ppro.exception.UserAlreadyExistsException;
import cz.jobs.ppro.model.User;
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
    private PasswordEncoder passwordEncoder;
    @Override
    public void registerUser(User user) {

        String username = user.getUsername();
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(username));

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with the username '%s' already exists.", username));
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());


        User newUser = new User(user.getUsername(), hashedPassword, user.getRole());
        userRepository.save(newUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if(user == null) {
           throw new UsernameNotFoundException("Uživatel s uživatelským jménem " + username + " nenalezen");
        }
        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
