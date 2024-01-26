package cz.jobs.ppro.service;

import cz.jobs.ppro.exception.UserAlreadyExistsException;
import cz.jobs.ppro.exception.UserNotFoundException;
import cz.jobs.ppro.model.*;
import cz.jobs.ppro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private SeekerRepository seekerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersonalDataRepository personalDataRepository;
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private WorkingExperienceRepository workingExperienceRepository;

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
        else if(user.getRole().equals("SEEKER")) {
            User newUser = new User(user.getUsername(), hashedPassword, user.getRole());
            userRepository.save(newUser);

            PersonalData personalData = new PersonalData();
            personalDataRepository.save(personalData);

            CV cv = new CV(personalData);
            cvRepository.save(cv);

            Seeker newSeeker = new Seeker(newUser, cv);
            seekerRepository.save(newSeeker);
        }


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

    @Override
    public User findUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public Seeker findSeekerByUserId(Long id) {
        return seekerRepository.findByUserId(id);
    }

    public void savePersonalData(PersonalData personalData) {
       personalDataRepository.save(personalData);
    }
    public void saveCV(CV cv) {
        cvRepository.save(cv);
    }
    public void saveSeeker(Seeker seeker) {
        seekerRepository.save(seeker);
    }
    public Manager findManagerById(Long id) {
        return managerRepository.getReferenceById(id);
    }
    public void saveEducation(Education education){
        educationRepository.save(education);
    }

    public void deleteEducationsByCvId(Long cvId) {
        educationRepository.deleteByCvId(cvId);
    }
    public void deleteExperiencesByCvId(Long cvId) {
        workingExperienceRepository.deleteByCvId(cvId);
    }
    public void saveExperience(WorkingExperience workingExperience){
        workingExperienceRepository.save(workingExperience);
    }
    public CV getCvById(Long id) {
        return cvRepository.getReferenceById(id);
    }

    public void saveManager(Manager manager){
        managerRepository.save(manager);
    }
    public Manager findManagerByUserId(Long userId){
        return managerRepository.findByUserId(userId);
    }
}
