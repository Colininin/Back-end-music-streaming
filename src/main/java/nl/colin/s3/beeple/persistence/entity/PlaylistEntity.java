package nl.colin.s3.beeple.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    @JsonManagedReference
    private Set<SongEntity> songs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"playlists"})
    private UserEntity user;

    public PlaylistEntity(Long id, String name, Set<SongEntity> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs != null ? songs : new HashSet<>();
    }

    public PlaylistEntity() {}

    public Long getId() {return this.id;}
    public String getName() {return this.name;}
    public Set<SongEntity> getSongs() {return this.songs;}
    public UserEntity getUser() {return this.user;}

    public void setId(Long id) {this.id = id;}
    public void setName(@NotBlank String name) {this.name = name;}
    public void setSongs(Set<SongEntity> songs) {this.songs = songs;}
    public void setUser(UserEntity user) {this.user = user;}

}
