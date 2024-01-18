package cz.jobs.ppro.service;

import cz.jobs.ppro.model.User;

public interface UserService {
    void registerUser(User user);
    User findByUsername(String username);
}
