package net.mihir.journalapp.Controller; // Sonar will ask to rename to 'controller'

import io.swagger.v3.oas.annotations.tags.Tag;
import net.mihir.journalapp.DTO.UserUpdateDto; // <-- IMPORT NEW DTO
import net.mihir.journalapp.api.response.WeatherResponse;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.services.UserServices;
import net.mihir.journalapp.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT THIS
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("user")
@Tag(name = "User APIs")
public class UserController {

    @Autowired
    UserServices userServices;
    @Autowired
    WeatherService weatherService;

    // 1. INJECT THE PASSWORD ENCODER
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PutMapping
    // 2. USE DTO AND SPECIFIC RESPONSE TYPE
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uname = authentication.getName();

        User userByName = userServices.findUserByName(uname);
        if (userByName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 3. SET FIELDS FROM DTO
        userByName.setUserName(userDto.getUserName());
        // 4. USE INJECTED ENCODER AND DTO PASSWORD
        userByName.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 5. CALL THE NEW SAVE METHOD
        userServices.saveUser(userByName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    // 6. USE SPECIFIC RESPONSE TYPE
    public  ResponseEntity<Void> deleteUserFromName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uname = authentication.getName();

        User userByName = userServices.findUserByName(uname);
        if (userByName == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userServices.deleteUserEntry(uname);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    // 7. USE SPECIFIC RESPONSE TYPE
    public  ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse response = weatherService.getWeather("Mumbai");
        String greeting = "";
        if(response != null){
            greeting = " , weather feels like " + response.getCurrent().getFeelsLike();
        }
        return new ResponseEntity<>("hi " + authentication.getName() + greeting ,HttpStatus.OK);
    }
}