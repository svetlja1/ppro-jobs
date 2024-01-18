package cz.jobs.ppro.service;

import cz.jobs.ppro.model.User;
import cz.jobs.ppro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        // Přidejte logiku pro kontrolu existence uživatele, validaci hesla, a uložení do databáze.
        // Např. userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
