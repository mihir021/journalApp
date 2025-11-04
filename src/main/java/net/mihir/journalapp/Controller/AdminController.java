package net.mihir.journalapp.Controller; // <-- Sonar will ask to rename this to 'controller'

import io.swagger.v3.oas.annotations.tags.Tag;
import net.mihir.journalapp.DTO.UserDTO;
import net.mihir.journalapp.entity.User;
import net.mihir.journalapp.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {

    @Autowired
    UserServices userServices;

    @GetMapping("/all-users")
    // FIX for wildcard issue:
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allEntry = userServices.getAllEntry();
        if(allEntry != null && !allEntry.isEmpty()){
            return new ResponseEntity<>(allEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(allEntry, HttpStatus.NOT_FOUND);
    }

    /*this method is use for add admin in database
     * */
    @PostMapping("/create-admin")
    // FIX for security issue:
    public void createUser(@RequestBody UserDTO userDto){
        userServices.saveAdmin(userDto);
    }
}