package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.PlaylistService;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.persistence.PlaylistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    @Override
    @Transactional
    public Playlist createPlaylist(String name, User user) {
        return playlistRepository.createPlaylist(name, user);
    }

    @Override
    @Transactional
    public List<Playlist> getPlaylistsForUser(Long id) {
        return playlistRepository.getPlaylistsForUser(id);
    }

    @Override
    @Transactional
    public List<Song> getSongsForPlaylist(Long playlistId) {
        return playlistRepository.getSongsForPlaylist(playlistId);
    }

    @Override
    @Transactional
    public Playlist getPlaylistInfo(Long playlistId) {
        return playlistRepository.getPlaylist(playlistId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> addSongToPlaylist(Long playlistId, Long songId) {
        if (playlistRepository.addSongToPlaylist(playlistId, songId)) {
            return ResponseEntity.ok("Song " + songId + " added to " + playlistId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deletePlaylist(Long playlistId) {
        if(playlistRepository.deletePlaylist(playlistId)){
            return ResponseEntity.ok("Playlist " + playlistId + " deleted");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}

