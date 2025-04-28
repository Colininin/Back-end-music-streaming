package nl.colin.s3.beeple.persistence;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.persistence.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepositoryJPA extends JpaRepository<SongEntity, String> {
    boolean existsByArtistIdAndTitle(@Param("artistId")Long artistId, @Param("title")String title);
    SongEntity findById(@Param("id") Long id);
    List<SongEntity> findByTitleContainingIgnoreCase(@Param("title")String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_song WHERE song_id = :songId AND playlist_id = :playlistId", nativeQuery = true)
    int removeSongFromPlaylist(@Param("songId") Long songId, @Param("playlistId") Long playlistId);


    @Query(value = "SELECT s.id AS id, s.title AS title, s.song_path AS songPath, s.artist_id AS artistId, COUNT(ps.playlist_id) AS playlistCount FROM playlist_song ps JOIN song_entity s ON ps.song_id = s.id GROUP BY s.id, s.id, s.title, s.song_path, s.artist_id", nativeQuery = true)
    List<Object[]> getCountOfAppearances();
}
