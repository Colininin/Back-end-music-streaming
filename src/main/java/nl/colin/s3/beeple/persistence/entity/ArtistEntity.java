package nl.colin.s3.beeple.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String name;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<SongEntity> songs = new HashSet<>();

    public ArtistEntity(Long id, @NotBlank String name, Set<SongEntity> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    public ArtistEntity() {}

    public Long getId() {return this.id;}
    public @NotBlank String getName() {return this.name;}
    public Set<SongEntity> getSongs() { return this.songs; }

    public void setId(Long id) {this.id = id;}
    public void setName(@NotBlank String name) {this.name = name;}
    public void setSongs(Set<SongEntity> songs) { this.songs = songs; }
}
