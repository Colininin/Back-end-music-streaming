package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public CreateUserRequest() {}

    public @NotBlank String getName() {
        return this.name;
    }
    public @NotBlank String getEmail() {
        return this.email;
    }
    public @NotBlank String getPassword() {
        return this.password;
    }

    public void setName(@NotBlank String name) {this.name = name;}
    public void setEmail(@NotBlank String email) {this.email = email;}
    public void setPassword(@NotBlank String password) {this.password = password;}
}
