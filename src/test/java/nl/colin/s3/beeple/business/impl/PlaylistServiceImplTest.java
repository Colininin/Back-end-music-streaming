package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.persistence.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@Transactional
class PlaylistServiceImplTest {

    @MockBean
    private PlaylistRepository playlistRepository;

    @Test
    void createPlaylist_test() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        User user = new User(1L, "John Doe");
        Playlist expectedPlaylist = new Playlist(1L, "My Playlist", null);

        Mockito.when(playlistRepository.createPlaylist(any(String.class), any(User.class))).thenReturn(expectedPlaylist);

        Playlist result = playlistService.createPlaylist("My Playlist", user);

        assertEquals(expectedPlaylist.getId(), result.getId());
        assertEquals(expectedPlaylist.getName(), result.getName());
    }

    @Test
    void getPlaylistsForUser_test() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Playlist playlist1 = new Playlist(1L, "Playlist 1", null);
        Playlist playlist2 = new Playlist(2L, "Playlist 2", null);

        List<Playlist> expectedPlaylists = Arrays.asList(playlist1, playlist2);

        Mockito.when(playlistRepository.getPlaylistsForUser(anyLong())).thenReturn(expectedPlaylists);

        List<Playlist> result = playlistService.getPlaylistsForUser(1L);

        assertEquals(expectedPlaylists.size(), result.size());
        assertEquals(expectedPlaylists.get(0).getName(), result.get(0).getName());
        assertEquals(expectedPlaylists.get(1).getName(), result.get(1).getName());
    }

    @Test
    void getSongsForPlaylist_test() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Artist artist1 = new Artist(1L, "Artist 1");
        Artist artist2 = new Artist(2L, "Artist 2");
        Song song1 = new Song(1L, artist1, "Artist 1", "songs/test/test.mp3");
        Song song2 = new Song(2L, artist2, "Artist 2", "songs/test/test2.mp3");

        List<Song> expectedSongs = Arrays.asList(song1, song2);

        Mockito.when(playlistRepository.getSongsForPlaylist(anyLong())).thenReturn(expectedSongs);

        List<Song> result = playlistService.getSongsForPlaylist(1L);

        assertEquals(expectedSongs.size(), result.size());
        assertEquals(expectedSongs.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expectedSongs.get(1).getTitle(), result.get(1).getTitle());
    }

    @Test
    void getPlaylistInfo_test() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Playlist expectedPlaylist = new Playlist(1L, "My Playlist", null);

        Mockito.when(playlistRepository.getPlaylist(anyLong())).thenReturn(expectedPlaylist);

        Playlist result = playlistService.getPlaylistInfo(1L);

        assertEquals(expectedPlaylist.getId(), result.getId());
        assertEquals(expectedPlaylist.getName(), result.getName());
    }

    @Test
    void addSongToPlaylist_test_success() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Mockito.when(playlistRepository.addSongToPlaylist(anyLong(), anyLong())).thenReturn(true);

        ResponseEntity<String> response = playlistService.addSongToPlaylist(1L, 1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Song 1 added to 1", response.getBody());
    }

    @Test
    void addSongToPlaylist_test_fail() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Mockito.when(playlistRepository.addSongToPlaylist(anyLong(), anyLong())).thenReturn(false);

        ResponseEntity<String> response = playlistService.addSongToPlaylist(1L, 1L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void deletePlaylist_test_success() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Mockito.when(playlistRepository.deletePlaylist(anyLong())).thenReturn(true);

        ResponseEntity<String> response = playlistService.deletePlaylist(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Playlist 1 deleted", response.getBody());
    }

    @Test
    void deletePlaylist_test_fail() {
        PlaylistServiceImpl playlistService = new PlaylistServiceImpl(playlistRepository);
        Mockito.when(playlistRepository.deletePlaylist(anyLong())).thenReturn(false);

        ResponseEntity<String> response = playlistService.deletePlaylist(1L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}