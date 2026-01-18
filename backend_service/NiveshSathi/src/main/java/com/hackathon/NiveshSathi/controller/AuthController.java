package com.hackathon.NiveshSathi.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.hackathon.NiveshSathi.dto.AuthResponse;
import com.hackathon.NiveshSathi.dto.GoogleAuthRequest;
import com.hackathon.NiveshSathi.dto.LoginRequest;
import com.hackathon.NiveshSathi.dto.SignupRequest;
import com.hackathon.NiveshSathi.entity.User;
import com.hackathon.NiveshSathi.repository.UserRepository;
import com.hackathon.NiveshSathi.service.AuthService;
import com.hackathon.NiveshSathi.service.GoogleAuthService;
import com.hackathon.NiveshSathi.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(
            GoogleAuthService googleAuthService,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.googleAuthService = googleAuthService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(
            @RequestBody GoogleAuthRequest request
    ) {
        GoogleIdToken.Payload payload =
                googleAuthService.verify(request.getToken());

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String googleId = payload.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(email);
                    u.setName(name);
                    u.setGoogleId(googleId);
                    u.setProvider("GOOGLE");
                    u.setProfession(""); // or default
                    return userRepository.save(u);
                });

        String jwt = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new AuthResponse(
                        jwt,
                        user.getName(),
                        user.getEmail(),
                        user.getProfession()
                )
        );
    }
}
