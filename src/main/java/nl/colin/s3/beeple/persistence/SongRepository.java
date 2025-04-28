package nl.colin.s3.beeple.persistence;

import nl.colin.s3.beeple.domain.Song;
import java.util.List;
import java.util.Map;

public interface SongRepository {
    boolean existsByArtistAndTitle(Long artistId, String title);

    List<Song> findAll();
    Song save(Long artistId, String title, String songPath);
    List<Song> findByTitleContainingIgnoreCase(String title);
    Song findById(Long id);
    boolean removeSongFromPlaylist(Long songId, Long playlistId);
    Map<String, Map<String, Object>> getAmountOfAppearances();
}
