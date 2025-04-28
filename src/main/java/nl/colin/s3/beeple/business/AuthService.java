package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.controller.dto.*;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    String register(CreateUserRequest createUserRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
