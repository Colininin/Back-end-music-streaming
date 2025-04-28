package nl.colin.s3.beeple.controller;

import nl.colin.s3.beeple.controller.dto.AuthRequest;
import nl.colin.s3.beeple.controller.dto.AuthResponse;
import nl.colin.s3.beeple.controller.dto.CreateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import nl.colin.s3.beeple.business.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public String register(@RequestBody CreateUserRequest createUserRequest){

        return authService.register(createUserRequest);
    }
    
}
