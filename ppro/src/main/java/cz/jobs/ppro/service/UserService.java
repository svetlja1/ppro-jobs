package cz.jobs.ppro.service;

import cz.jobs.ppro.model.*;
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
    void savePersonalData(PersonalData personalData);

    void saveCV(CV cv);
    void saveSeeker(Seeker seeker);
    void saveEducation(Education education);
    void deleteEducationsByCvId(Long cvId);

    void deleteExperiencesByCvId(Long cvId);
    void saveExperience(WorkingExperience workingExperience);

    void saveManager(Manager manager);

    Manager findManagerByUserId(Long userId);
    CV getCvById(Long id);
}
