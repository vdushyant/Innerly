package net.dushyant.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.dushyant.journalApp.entity.User;
import net.dushyant.journalApp.service.JwtService;
import net.dushyant.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @GetMapping("health-check")
    public String healthCheck(){ return "ok"; }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        if (user.getUserName() == null || user.getUserName().trim().length() < 3
                || user.getPassword() == null || user.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Name must be at least 3 characters and password at least 6 characters.");
        }
        if (userService.saveNewUser(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with that name already exists.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        if (user.getUserName() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Name and password are required.");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(Collections.singletonMap("token", jwtService.generateToken(userDetails)));
        } catch (org.springframework.security.core.AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Name or password is incorrect.");
        }
    }
}
