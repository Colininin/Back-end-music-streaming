package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.controller.dto.SearchRequest;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.persistence.PlaylistRepository;
import nl.colin.s3.beeple.persistence.SongRepository;
import nl.colin.s3.beeple.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class SearchServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    public SearchServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetResults_success() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setInput("test");
        Artist artist = new Artist(1L, "artist");
        Song song = new Song(1L, artist, "test title", "songs/test/test/test/test/test.mp3");
        song.setTitle("Test Song");

        User user = new User(1L, "John", "john@gmail.com", "password", "USER");
        user.setName("Test User");

        Playlist playlist = new Playlist(1L, "Test Playlist", null);
        playlist.setName("Test Playlist");

        when(songRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(song));
        when(userRepository.findByNameContainingIgnoreCase("test")).thenReturn(List.of(user));
        when(playlistRepository.findByNameContainingIgnoreCase("test")).thenReturn(List.of(playlist));

        Map<String, Object> results = searchService.getResults(searchRequest);

        assertThat(results).containsKeys("songs", "users", "playlists");
        assertThat(results.get("songs")).isNotNull();
        assertThat(results.get("users")).isNotNull();
        assertThat(results.get("playlists")).isNotNull();
    }

    @Test
    void testGetResults_empty() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setInput("test");

        when(songRepository.findByTitleContainingIgnoreCase("test")).thenReturn(Collections.emptyList());
        when(userRepository.findByNameContainingIgnoreCase("test")).thenReturn(Collections.emptyList());
        when(playlistRepository.findByNameContainingIgnoreCase("test")).thenReturn(Collections.emptyList());

        Map<String, Object> results = searchService.getResults(searchRequest);

        assertThat(results).containsKeys("songs", "users", "playlists");
        assertThat(results.get("songs")).isEqualTo(Collections.emptyList());
        assertThat(results.get("users")).isEqualTo(Collections.emptyList());
        assertThat(results.get("playlists")).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetResults_fail() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setInput("");

        when(songRepository.findByTitleContainingIgnoreCase("")).thenReturn(Collections.emptyList());
        when(userRepository.findByNameContainingIgnoreCase("")).thenReturn(Collections.emptyList());
        when(playlistRepository.findByNameContainingIgnoreCase("")).thenReturn(Collections.emptyList());

        Map<String, Object> results = searchService.getResults(searchRequest);

        assertThat(results).containsKeys("songs", "users", "playlists");
        assertThat(results.get("songs")).isEqualTo(Collections.emptyList());
        assertThat(results.get("users")).isEqualTo(Collections.emptyList());
        assertThat(results.get("playlists")).isEqualTo(Collections.emptyList());
    }
}