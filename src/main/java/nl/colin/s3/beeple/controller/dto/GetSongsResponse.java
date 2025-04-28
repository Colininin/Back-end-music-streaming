package nl.colin.s3.beeple.controller.dto;


import java.util.ArrayList;
import java.util.List;

public class GetSongsResponse {
    public GetSongsResponse() {}

    private List<SongDTO> songs = new ArrayList<>();
    public List<SongDTO> getSongs() {
        return this.songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}
