package net.mihir.journalapp.services;

import lombok.extern.slf4j.Slf4j;
import net.mihir.journalapp.DTO.UserDTO; // Import the DTO
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.repo.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Import the interface
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ... (saveNewUser method)
    public void saveNewUser(UserDTO userDto) {
        User newUser = new User();
        newUser.setUserName(userDto.getUserName());
        newUser.setEmail(userDto.getEmail());
        newUser.setSentimentAnalysis(userDto.isSentimentAnalysis());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash password from DTO
        newUser.setRoles(List.of("USER"));
        userRepository.save(newUser);
    }

    // ... (saveAdmin method)
    public void saveAdmin(UserDTO userDto) {
        try {
            User newAdmin = new User();
            newAdmin.setUserName(userDto.getUserName());
            newAdmin.setEmail(userDto.getEmail());
            newAdmin.setSentimentAnalysis(userDto.isSentimentAnalysis());
            newAdmin.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash password from DTO
            newAdmin.setRoles(List.of("USER", "ADMIN"));
            userRepository.save(newAdmin);
        } catch (Exception e) {
            log.error("error for user name {}", userDto.getUserName(), e);
        }
    }

    public List<User> getAllEntry() {
        return userRepository.findAll();
    }

    public Optional<User> findByUserId(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteUserEntry(String userName) {
        userRepository.deleteByUserName(userName);
    }


    public User findUserByName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}