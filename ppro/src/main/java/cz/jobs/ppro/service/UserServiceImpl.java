package cz.jobs.ppro.service;

import cz.jobs.ppro.exception.UserAlreadyExistsException;
import cz.jobs.ppro.model.User;
import cz.jobs.ppro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

        User newUser = new User(user.getUsername(), hashedPassword);
        userRepository.save(newUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
           throw new UsernameNotFoundException("Uživatel s uživatelským jménem " + username + " nenalezen");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Zde by mělo být již zakódované heslo z databáze
                .roles("USER") // Případně můžete nastavit role uživatele
                .build();
    }
}
