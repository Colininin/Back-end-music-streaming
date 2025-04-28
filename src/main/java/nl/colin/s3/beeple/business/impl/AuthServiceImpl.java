package nl.colin.s3.beeple.business.impl;


import nl.colin.s3.beeple.business.AuthService;
import nl.colin.s3.beeple.controller.dto.AuthRequest;
import nl.colin.s3.beeple.controller.dto.AuthResponse;
import nl.colin.s3.beeple.controller.dto.CreateUserRequest;
import nl.colin.s3.beeple.controller.dto.RefreshTokenRequest;
import nl.colin.s3.beeple.persistence.UserRepository;
import nl.colin.s3.beeple.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import nl.colin.s3.beeple.domain.User;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepo = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepo.findByEmail(authRequest.getEmail());
        if (user == null || !passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole());

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public String register(CreateUserRequest createUserRequest) {
        if (userRepo.existsByEmail(createUserRequest.getEmail())) {
            return "User already exists with this email";
        }
        String hashedPass = passwordEncoder.encode(createUserRequest.getPassword());
        userRepo.save(createUserRequest.getName(), createUserRequest.getEmail(), hashedPass);
        return "User registered successfully";
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        if (jwtUtil.validateToken(refreshTokenRequest.getRefreshToken())) {
            String newAccessToken = jwtUtil.generateToken(jwtUtil.extractEmail(refreshTokenRequest.getRefreshToken()), jwtUtil.extractRole(refreshTokenRequest.getRefreshToken()));
            return new AuthResponse(newAccessToken, refreshTokenRequest.getRefreshToken());
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }
}
