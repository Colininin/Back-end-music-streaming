package nl.colin.s3.beeple.persistence.impl;

import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.business.exception.SongAlreadyExists;
import nl.colin.s3.beeple.persistence.ArtistRepository;
import nl.colin.s3.beeple.persistence.SongRepository;
import nl.colin.s3.beeple.persistence.entity.SongEntity;
import nl.colin.s3.beeple.persistence.entity.ArtistEntity;

import nl.colin.s3.beeple.persistence.SongRepositoryJPA;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SongRepositoryImpl implements SongRepository {
    private final SongRepositoryJPA jpaRepo;
    private final ArtistRepository artistRepo;

    public SongRepositoryImpl(SongRepositoryJPA jpaRepo, ArtistRepository artistRepo) {
        this.jpaRepo = jpaRepo;
        this.artistRepo = artistRepo;
    }

    @Override
    public boolean existsByArtistAndTitle(Long artistId, String title){
        return jpaRepo.existsByArtistIdAndTitle(artistId,title);
    }

    @Override
    public List<Song> findAll(){
        List<SongEntity> songs = jpaRepo.findAll();
        return songs.stream().map(this::convertToDomain).toList();
    }

    @Override
    public Song save(Long artistId, String title, String songPath){
        ArtistEntity artist = artistRepo.findEntityById(artistId);
        SongEntity song = new SongEntity();

        song.setArtist(artist);
        song.setTitle(title);
        song.setSongPath(songPath);

        try{
            SongEntity newSong = jpaRepo.save(song);
            return convertToDomain(newSong);
        }
        catch (Exception e){
            throw new SongAlreadyExists();
        }
    }

    @Override
    public List<Song> findByTitleContainingIgnoreCase(String title){
        List<SongEntity> songs = jpaRepo.findByTitleContainingIgnoreCase(title);
        return songs.stream().map(this::convertToDomain).toList();
    }

    @Override
    public Song findById(Long songId){
        SongEntity song = jpaRepo.findById(songId);
        if (song == null) {
            throw new IllegalArgumentException("Song not found with id: " + songId);
        }
        return convertToDomain(song);
    }

    @Override
    public boolean removeSongFromPlaylist(Long songId, Long playlistId) {
        int rowsAffected = jpaRepo.removeSongFromPlaylist(songId, playlistId);
        return rowsAffected > 0;
    }

    @Override
    public Map<String, Map<String, Object>> getAmountOfAppearances() {
        List<Object[]> results = jpaRepo.getCountOfAppearances();
        Map<String, Map<String, Object>> songCountMap = new LinkedHashMap<>();

        int songIndex = 1;

        for (Object[] row : results) {
            SongEntity songEntity = new SongEntity();
            songEntity.setId(((Number) row[0]).longValue());
            songEntity.setTitle((String) row[1]);
            songEntity.setSongPath((String) row[2]);
            Long artistId = ((Number) row[3]).longValue();
            songEntity.setArtist(artistRepo.findEntityById(artistId));
            Long count = ((Number)row[4]).longValue();
            Map<String, Object> songData = new HashMap<>();
            songData.put("id", songEntity.getId());
            songData.put("title", songEntity.getTitle());
            songData.put("artist", songEntity.getArtist().getName());
            songData.put("count", count);

            songCountMap.put("song" + songIndex++, songData);
        }

        return songCountMap;
    }

    private Song convertToDomain(SongEntity songEntity){
        return new Song(songEntity.getId(), convertArtistEntityToDomain(songEntity.getArtist()), songEntity.getTitle(), songEntity.getSongPath());
    }

    private Artist convertArtistEntityToDomain(ArtistEntity artistEntity){
        return new Artist(artistEntity.getId(), artistEntity.getName());
    }
}
