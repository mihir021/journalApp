package net.mihir.journalapp.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.mihir.journalapp.utilis.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import net.mihir.journalapp.DTO.LoginRequestDto;
import net.mihir.journalapp.DTO.UserDTO;
import net.mihir.journalapp.services.UserServices;
import net.mihir.journalapp.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs")
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserServices userServices;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    // FIX 2 (Signup Logic): Simplified to one line
    public void signup(@RequestBody UserDTO user) {
        // The service now handles all mapping and password hashing
        userServices.saveNewUser(user);
    }

    @GetMapping("/test")
    public String test(){
        return "all set";
    }

    @PostMapping("/login")
    // FIX 1 (Security): Changed from User to LoginRequestDto
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}