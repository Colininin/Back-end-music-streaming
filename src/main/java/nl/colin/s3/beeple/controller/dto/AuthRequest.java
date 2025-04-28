package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public AuthRequest() {}

    public @NotBlank String getEmail() {
        return this.email;
    }
    public @NotBlank String getPassword() {
        return this.password;
    }

    public void setEmail(@NotBlank String email) {this.email = email;}
    public void setPassword(@NotBlank String password) {this.password = password;}
}
