package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.ArtistService;
import nl.colin.s3.beeple.business.exception.SongAlreadyExists;
import nl.colin.s3.beeple.business.exception.SongNotFound;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.SongRepository;
import nl.colin.s3.beeple.domain.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class SongServiceImplTest {
    @Mock
    private SongRepository songRepository;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private SongServiceImpl songService;

    String artistName = "Test Artist";
    String title = "Test Title";
    String songPath = "/songs/test/test";

    @Test
    void createSong_success() {

        Artist artist = new Artist(1L, artistName);
        Song savedSong = new Song(1L, artist, title, songPath);

        when(artistService.createArtist(artistName)).thenReturn(artist);
        when(songRepository.existsByArtistAndTitle(artist.getId(), title)).thenReturn(false);
        when(songRepository.save(artist.getId(), title, songPath)).thenReturn(savedSong);

        Song createdSong = songService.createSong(artistName, title, songPath);

        assertNotNull(createdSong);
        assertEquals(artist, createdSong.getArtist());
        assertEquals(title, createdSong.getTitle());
    }

    @Test
    void createSong_AlreadyExists() {

        Artist artist = new Artist(1L, artistName);

        when(artistService.createArtist(artistName)).thenReturn(artist);
        when(songRepository.existsByArtistAndTitle(artist.getId(), title)).thenReturn(true);

        assertThrows(SongAlreadyExists.class, () -> {
            songService.createSong(artistName, title, songPath);
        });

        verify(songRepository, never()).save(anyLong(), anyString(), anyString());
    }

    @Test
    void testGetSongs() {
        Artist artist = new Artist(1L, "Test Artist");
        Artist artist2 = new Artist(2L, "Test Artist 2");
        Song song1 = new Song(1L, artist, title, songPath);
        Song song2 = new Song(2L, artist2, title+'1', songPath+'1');

        List<Song> songs = List.of(song1, song2);
        when(songRepository.findAll()).thenReturn(songs);

        List<Song> result = songService.getSongs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(song1));
        assertTrue(result.contains(song2));
    }

    @Test
    void testGetSongById() {
        Artist artist = new Artist(1L, "Test Artist");
        Song song = new Song(1L, artist, title, songPath);
        when(songRepository.findById(1L)).thenReturn(song);
        assertEquals(song, songService.getSongById(1L));
    }

    @Test
    void removeSongFromPlaylist_success() {
        Long songId = 1L;
        Long playlistId = 101L;

        when(songRepository.removeSongFromPlaylist(songId, playlistId)).thenReturn(true);

        ResponseEntity<String> response = songService.removeSongFromPlaylist(songId, playlistId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Removed " + songId + " from " + playlistId, response.getBody());
    }

    @Test
    void removeSongFromPlaylist_songNotFound() {
        Long songId = 1L;
        Long playlistId = 101L;

        when(songRepository.removeSongFromPlaylist(songId, playlistId)).thenReturn(false);

        assertThrows(SongNotFound.class, () -> {
            songService.removeSongFromPlaylist(songId, playlistId);
        });
    }
}