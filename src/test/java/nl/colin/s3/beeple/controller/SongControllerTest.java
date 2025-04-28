package nl.colin.s3.beeple.controller;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.BeepleApplication;
import nl.colin.s3.beeple.business.FileStorageService;
import nl.colin.s3.beeple.business.SongService;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.domain.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BeepleApplication.class)
@Transactional
class SongControllerTest {

    @Mock
    SongService songService;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    private SongController songController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
    }

    @Test
    void getSongs_success() throws Exception {
        Artist artist1 = new Artist(1L, "artist1");
        Artist artist2 = new Artist(2L, "artist2");
        Song song = new Song(1L, artist1, "test title", "songs/test/test.mp3");
        Song song2 = new Song(2L, artist2, "test title2", "songs/test/test2.mp3");
        List<Song> songs = new ArrayList<>();
        songs.add(song);
        songs.add(song2);
        when(songService.getSongs()).thenReturn(songs);

        mockMvc.perform(get("/songs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs[0].id").value(1))
                .andExpect(jsonPath("$.songs[0].artist.name").value("artist1"))
                .andExpect(jsonPath("$.songs[0].title").value("test title"))
                .andExpect(jsonPath("$.songs[0].songPath").value("songs/test/test.mp3"))
                .andExpect(jsonPath("$.songs[1].id").value(2))
                .andExpect(jsonPath("$.songs[1].artist.name").value("artist2"))
                .andExpect(jsonPath("$.songs[1].title").value("test title2"))
                .andExpect(jsonPath("$.songs[1].songPath").value("songs/test/test2.mp3"));
    }

    @Test
    void createSong_success() throws Exception {
        Artist artist = new Artist(1L, "artist");
        String title = "Test Title";
        String songPath = "/path/to/song/file.mp3";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.mp3", "audio/mpeg", "dummy data".getBytes());

        Song song = new Song(1L, artist, title, songPath);

        when(fileStorageService.storeFile(eq(artist.getName()), any(MultipartFile.class))).thenReturn(songPath);
        when(songService.createSong(artist.getName(), title, songPath)).thenReturn(song);

        mockMvc.perform(multipart("/songs")
                        .file(mockFile)
                        .param("artist", artist.getName())
                        .param("title", title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getSong_success() throws Exception {
        Artist artist = new Artist(1L, "artist");
        Long songId = 1L;
        String songPath = "/path/to/song/file.mp3";
        Song song = new Song(songId, artist, "Test Title", songPath);
        Resource mockResource = new ByteArrayResource("dummy audio data".getBytes());

        when(songService.getSongById(songId)).thenReturn(song);
        when(fileStorageService.returnSongFile(songPath))
                .thenReturn(ResponseEntity.ok()
                        .header("Content-Type", "application/octet-stream")
                        .body(mockResource));

        mockMvc.perform(get("/songs/{songId}", songId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/octet-stream"))
                .andExpect(content().bytes("dummy audio data".getBytes()));
    }

    @Test
    void getSong_notFound() throws Exception {
        Long songId = 999L;

        when(songService.getSongById(songId)).thenReturn(null);

        mockMvc.perform(get("/songs/{songId}", songId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
