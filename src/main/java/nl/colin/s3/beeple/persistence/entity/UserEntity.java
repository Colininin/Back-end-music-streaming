package nl.colin.s3.beeple.persistence.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private String email;

    @NotBlank
    private String password;

    private String role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlaylistEntity> playlists = new HashSet<>();

    public UserEntity(Long id, @NotBlank String name, @NotBlank String email, @NotBlank String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "USER";
    }

    public UserEntity() {}

    public Long getId() {return this.id;}
    public @NotBlank String getName() {return this.name;}
    public @NotBlank String getEmail() {return this.email;}
    public @NotBlank String getPassword() {return this.password;}
    public String getRole() {return this.role;}

    public void setId(Long id) {this.id = id;}
    public void setName(@NotBlank String name) {this.name = name;}
    public void setEmail(@NotBlank String email) {this.email = email;}
    public void setPassword(@NotBlank String password) {this.password = password;}
    public void setRole(@NotBlank String role) {this.role = role;}

}
