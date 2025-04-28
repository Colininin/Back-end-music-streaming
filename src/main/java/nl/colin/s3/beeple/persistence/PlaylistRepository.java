package nl.colin.s3.beeple.persistence;

import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;

import java.util.List;

public interface PlaylistRepository {
    Playlist createPlaylist(String name, User user);
    List<Playlist> getPlaylistsForUser(Long id);
    List<Playlist> findByNameContainingIgnoreCase(String name);
    List<Song> getSongsForPlaylist(Long playlistId);
    Playlist getPlaylist(Long playlistId);
    boolean addSongToPlaylist(Long playlistId, Long songId);
    boolean deletePlaylist(Long playlistId);
}
