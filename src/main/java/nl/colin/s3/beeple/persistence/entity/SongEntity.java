package nl.colin.s3.beeple.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Entity
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistEntity artist;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String title;

    @NotBlank
    private String songPath;

    @ManyToMany(mappedBy = "songs")
    private Set<PlaylistEntity> playlists;

    public SongEntity(Long id, ArtistEntity artist, @NotBlank String songPath ,@NotBlank String title) {
        this.id = id;
        this.artist = artist;
        this.songPath = songPath;
        this.title = title;
    }

    public SongEntity() {}

    public Long getId() {return this.id;}
    public ArtistEntity getArtist() { return this.artist; }
    public @NotBlank String getTitle() {return this.title;}
    public @NotBlank String getSongPath() {return this.songPath;}

    public void setId(Long id) {this.id = id;}
    public void setArtist(ArtistEntity artist) { this.artist = artist; }
    public void setTitle(@NotBlank String title) {this.title = title;}
    public void setSongPath(@NotBlank String songPath) {this.songPath = songPath;}
    public void setPlaylists(Set<PlaylistEntity> playlists) {this.playlists = playlists;}
}
