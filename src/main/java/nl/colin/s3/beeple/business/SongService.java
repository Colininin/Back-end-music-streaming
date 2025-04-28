package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.domain.Song;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SongService {
    Song createSong(String artist, String title, String songPath);
    Song getSongById(Long id);
    List<Song> getSongs();
    ResponseEntity<String> removeSongFromPlaylist(Long songId, Long playlistId);
    Map<String, Map<String, Object>> getAmountOfAppearances();
}
