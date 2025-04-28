package nl.colin.s3.beeple.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistsResponse {
    public GetPlaylistsResponse() {}
    private List<PlaylistDTO> playlists = new ArrayList<>();

    public List<PlaylistDTO> getPlaylists() {
        return this.playlists;
    }

    public void setPlaylists(List<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }
}
