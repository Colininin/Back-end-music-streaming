package nl.colin.s3.beeple.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.colin.s3.beeple.business.PlaylistService;
import nl.colin.s3.beeple.business.UserService;
import nl.colin.s3.beeple.controller.dto.CreatePlaylistRequest;
import nl.colin.s3.beeple.controller.dto.CreatePlaylistResponse;
import nl.colin.s3.beeple.controller.dto.PlaylistDTO;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.controller.mapper.PlaylistMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistController(PlaylistService playlistService , UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @PostMapping
    public CreatePlaylistResponse createPlaylist(@RequestBody CreatePlaylistRequest input, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.getUser(email);

        Playlist result = playlistService.createPlaylist(input.getName(), user);
        CreatePlaylistResponse response = new CreatePlaylistResponse();
        response.setId(result.getId());
        response.setName(result.getName());
        return response;
    }

    @GetMapping("/user-playlists")
    public List<PlaylistDTO> getUserPlaylists(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.getUser(email);

        List<Playlist> playlists = playlistService.getPlaylistsForUser(user.getId());
        return PlaylistMapper.mapPlaylistListToDTOList(playlists);
    }

    @GetMapping("/{playlistId}/songs")
    public Playlist getSongsForPlaylist(@PathVariable Long playlistId) {
        return playlistService.getPlaylistInfo(playlistId);
    }

    @PostMapping("/add-song/{playlistId}/{songId}")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId){
        return playlistService.addSongToPlaylist(playlistId, songId);
    }

    @DeleteMapping("/delete/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId) {
        return playlistService.deletePlaylist(playlistId);
    }

}
