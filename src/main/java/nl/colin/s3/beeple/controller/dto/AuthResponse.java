package nl.colin.s3.beeple.controller.dto;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {return this.accessToken;}
    public String getRefreshToken() {return this.refreshToken;}

    public void setAccessToken(String accessToken) {this.accessToken = accessToken;}
    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
}
