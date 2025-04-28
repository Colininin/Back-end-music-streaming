package nl.colin.s3.beeple.persistence.impl;

import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.persistence.PlaylistRepository;
import nl.colin.s3.beeple.persistence.PlaylistRepositoryJPA;
import nl.colin.s3.beeple.persistence.entity.ArtistEntity;
import nl.colin.s3.beeple.persistence.entity.PlaylistEntity;
import nl.colin.s3.beeple.persistence.entity.SongEntity;
import nl.colin.s3.beeple.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class PlaylistRepositoryImpl implements PlaylistRepository {
    private final PlaylistRepositoryJPA jpaRepo;

    public PlaylistRepositoryImpl(PlaylistRepositoryJPA jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Playlist createPlaylist(String name, User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setName(userEntity.getName());

        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setName(name);
        playlist.setUser(userEntity);

        try{
            PlaylistEntity newPlaylist = jpaRepo.save(playlist);
            return convertToDomain(newPlaylist);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Playlist> getPlaylistsForUser(Long id) {
        List<PlaylistEntity> playlists = jpaRepo.findByUserId(id);
        return playlists.stream().map(this::convertToDomain).toList();
    }

    @Override
    public List<Playlist> findByNameContainingIgnoreCase(String name) {
        List<PlaylistEntity> playlists = jpaRepo.findByNameContainingIgnoreCase(name);
        return playlists.stream().map(this::convertToDomain).toList();
    }

    @Override
    public List<Song> getSongsForPlaylist(Long playlistId) {
        PlaylistEntity playlistEntity = jpaRepo.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));

        return playlistEntity.getSongs().stream().map(this::convertSongEntityToDomain).toList();
    }

    @Override
    public Playlist getPlaylist(Long playlistId) {
        PlaylistEntity playlistEntity = jpaRepo.findById(playlistId).orElseThrow(() -> new RuntimeException("Playlist not found"));

        return convertToDomain(playlistEntity);
    }

    @Override
    public boolean addSongToPlaylist(Long playlistId, Long songId) {
        int rowsAffected = jpaRepo.addSongToPlaylist(playlistId, songId);
        return rowsAffected > 0;
    }

    @Override
    public boolean deletePlaylist(Long playlistId) {
        jpaRepo.deleteSongsFromPlaylist(playlistId);
        int rowsAffected = jpaRepo.deleteThisPlaylist(playlistId);
        return rowsAffected > 0;
    }


    private Playlist convertToDomain(PlaylistEntity playlistEntity){
        Set<Song> songs = playlistEntity.getSongs() != null
                ? playlistEntity.getSongs().stream()
                .map(this::convertSongEntityToDomain)
                .collect(Collectors.toSet())
                : new HashSet<>();

        return new Playlist(
                playlistEntity.getId(),
                playlistEntity.getName(),
                songs
        );
    }
    private Song convertSongEntityToDomain(SongEntity songEntity) {
        return new Song(songEntity.getId(), convertArtistEntityToDomain(songEntity.getArtist()), songEntity.getTitle(), songEntity.getSongPath());
    }
    private Artist convertArtistEntityToDomain(ArtistEntity artistEntity){
        return new Artist(artistEntity.getId(), artistEntity.getName());
    }
}
