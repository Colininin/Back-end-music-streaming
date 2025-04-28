package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {return this.refreshToken;}

    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
}
