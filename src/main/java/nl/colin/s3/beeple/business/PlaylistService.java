package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlaylistService {
    Playlist createPlaylist(String name, User user);
    List<Playlist> getPlaylistsForUser(Long id);
    List<Song> getSongsForPlaylist(Long id);
    Playlist getPlaylistInfo(Long id);
    ResponseEntity<String> addSongToPlaylist(Long playlistId, Long songId);
    ResponseEntity<String> deletePlaylist(Long playlistId);
}
