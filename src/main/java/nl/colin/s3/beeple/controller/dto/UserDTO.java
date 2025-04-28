package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.*;

public class UserDTO {
    private Long id;

    @NotNull(message = "Name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name must only contain letters and spaces")
    private String name;
    @Email(message = "Email should be a valid email address")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;
    private String role;

    public UserDTO() {}
    public Long getId() {return this.id;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
    public String getRole() {return this.role;}

    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setRole(String role) {this.role = role;}
}
