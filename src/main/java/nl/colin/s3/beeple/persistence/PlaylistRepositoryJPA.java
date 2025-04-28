package nl.colin.s3.beeple.persistence;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.persistence.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepositoryJPA  extends JpaRepository<PlaylistEntity, Long> {
    List<PlaylistEntity> findByUserId(@Param("id")Long id);
    List<PlaylistEntity> findByNameContainingIgnoreCase(@Param("name")String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO playlist_song (playlist_id, song_id) VALUES (:playlistId, :songId) ON DUPLICATE KEY UPDATE song_id = VALUES(song_id)", nativeQuery = true)
    int addSongToPlaylist(@Param("playlistId") Long playlistId, @Param("songId") Long songId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_song WHERE playlist_id = :playlistId", nativeQuery = true)
    int deleteSongsFromPlaylist(@Param("playlistId") Long playlistId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM playlist_entity WHERE id = :playlistId", nativeQuery = true)
    int deleteThisPlaylist(@Param("playlistId") Long playlistId);
}
