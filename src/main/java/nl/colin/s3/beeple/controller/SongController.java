package nl.colin.s3.beeple.controller;

import nl.colin.s3.beeple.business.FileStorageService;
import nl.colin.s3.beeple.business.SongService;
import nl.colin.s3.beeple.controller.dto.CreateSongResponse;
import nl.colin.s3.beeple.controller.dto.GetSongsResponse;
import nl.colin.s3.beeple.controller.mapper.SongMapper;
import nl.colin.s3.beeple.domain.Song;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;
    private final FileStorageService fileStorageService;

    public SongController(SongService songService, FileStorageService fileStorageService) {
        this.songService = songService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public GetSongsResponse getSongs() {
        List<Song> songs = songService.getSongs();
        return SongMapper.mapSongListToGetSongResponse(songs);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public CreateSongResponse createSong(@RequestParam("artist") String artist, @RequestParam("title") String title, @RequestParam("file") MultipartFile file) {
        String songPath = fileStorageService.storeFile(artist, file);

        Song result = songService.createSong(artist, title, songPath);
        CreateSongResponse output = new CreateSongResponse();
        output.setId(result.getId());
        return output;
    }

    @GetMapping("/{songId}")
    public ResponseEntity<Resource> getSong(@PathVariable Long songId) {
        Song song = songService.getSongById(songId);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        return fileStorageService.returnSongFile(song.getSongPath());
    }

    @DeleteMapping("/remove/{songId}")
    public ResponseEntity<String> removeSong(@PathVariable Long songId, @RequestParam Long playlistId) {
        return songService.removeSongFromPlaylist(songId, playlistId);
    }

    @GetMapping("/song-appearances")
    public Map<String, Map<String, Object>> getSongAppearances(){
        return songService.getAmountOfAppearances();
    }

}
