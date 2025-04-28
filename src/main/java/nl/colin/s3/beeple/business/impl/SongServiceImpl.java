package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.ArtistService;
import nl.colin.s3.beeple.business.exception.SongAlreadyExists;
import nl.colin.s3.beeple.business.exception.SongNotFound;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.SongRepository;
import nl.colin.s3.beeple.business.SongService;
import nl.colin.s3.beeple.domain.Song;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ArtistService artistService;
    public SongServiceImpl(SongRepository songRepository, ArtistService artistService) {
        this.songRepository = songRepository;
        this.artistService = artistService;
    }

    @Override
    @Transactional
    public Song createSong(String artistName, String title, String songPath){

        Artist artist = artistService.createArtist(artistName);

        if(songRepository.existsByArtistAndTitle(artist.getId(), title)){
            throw new SongAlreadyExists();
        }
        return songRepository.save(artist.getId(), title, songPath);
    }

    @Override
    @Transactional
    public List<Song> getSongs(){
        return songRepository.findAll();
    }

    @Override
    @Transactional
    public Song getSongById(Long songId){
        return songRepository.findById(songId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> removeSongFromPlaylist(Long songId, Long playlistId) {
        if (songRepository.removeSongFromPlaylist(songId, playlistId)) {
            return ResponseEntity.ok("Removed " + songId + " from " + playlistId);
        } else {
            throw new SongNotFound();
        }
    }

    @Override
    @Transactional
    public  Map<String, Map<String, Object>> getAmountOfAppearances(){
        return songRepository.getAmountOfAppearances();
    }
}
