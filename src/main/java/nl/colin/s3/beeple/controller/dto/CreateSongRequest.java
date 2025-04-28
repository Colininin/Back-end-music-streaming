package nl.colin.s3.beeple.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSongRequest {

    @NotBlank
    private String artist;
    @NotBlank
    private String title;
    @NotBlank
    private String songPath;

    public CreateSongRequest(){}

    public @NotBlank String getArtist() {
        return this.artist;
    }
    public @NotBlank String getTitle() {
        return this.title;
    }
    public @NotBlank String getSongPath(){
        return this.songPath;
    }

    public void setArtist(@NotBlank String artist) {this.artist = artist;}
    public void setTitle(@NotBlank String title) {this.title = title;}
    public void setSongPath(@NotBlank String songPath) {this.songPath = songPath;}
}
