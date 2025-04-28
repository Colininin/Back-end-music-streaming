package nl.colin.s3.beeple.controller.mapper;

import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.controller.dto.PlaylistDTO;
import nl.colin.s3.beeple.controller.dto.GetPlaylistsResponse;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMapper {
    public static GetPlaylistsResponse mapPlaylistListToGetPlaylistsResponse(List<Playlist> input) {
        List<PlaylistDTO> outputPlaylists = new ArrayList<>();

        for (Playlist playlist : input) {
            outputPlaylists.add(mapPlaylistToGetPlaylistResponse(playlist));
        }

        GetPlaylistsResponse response = new GetPlaylistsResponse();
        response.setPlaylists(outputPlaylists);
        return response;
    }

    public static List<PlaylistDTO> mapPlaylistListToDTOList(List<Playlist> input) {
        List<PlaylistDTO> outputPlaylists = new ArrayList<>();
        for (Playlist playlist : input) {
            outputPlaylists.add(mapPlaylistToGetPlaylistResponse(playlist));
        }
        return outputPlaylists;
    }

    public static PlaylistDTO mapPlaylistToGetPlaylistResponse(Playlist input) {
        PlaylistDTO response = new PlaylistDTO();
        response.setId(input.getId());
        response.setName(input.getName());
        return response;
    }
}
