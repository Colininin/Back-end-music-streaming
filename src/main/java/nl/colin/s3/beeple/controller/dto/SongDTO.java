package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotNull;
import nl.colin.s3.beeple.domain.Artist;

public class SongDTO {
    private Long id;
    @NotNull(message = "Artist cannot be null")
    private Artist artist;
    @NotNull(message = "Title cannot be null")
    private String title;
    private String songPath;

    public SongDTO(){}

    public Long getId() {return this.id;}
    public Artist getArtist() {return this.artist;}
    public String getTitle() {return this.title;}
    public String getSongPath() {return this.songPath;}

    public void setId(Long id) { this.id = id;}
    public void setArtist(Artist artist) { this.artist = artist;}
    public void setTitle(String title) { this.title = title;}
    public void setSongPath(String songPath) { this.songPath = songPath;}
}
