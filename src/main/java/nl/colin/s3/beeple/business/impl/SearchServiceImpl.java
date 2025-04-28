package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.SearchService;
import nl.colin.s3.beeple.controller.dto.SearchRequest;
import nl.colin.s3.beeple.controller.mapper.PlaylistMapper;
import nl.colin.s3.beeple.controller.mapper.SongMapper;
import nl.colin.s3.beeple.controller.mapper.UserMapper;
import nl.colin.s3.beeple.domain.Playlist;
import nl.colin.s3.beeple.domain.Song;
import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.persistence.PlaylistRepository;
import nl.colin.s3.beeple.persistence.SongRepository;
import nl.colin.s3.beeple.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    public SearchServiceImpl(UserRepository userRepository, SongRepository songRepository, PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    @Transactional
    public Map<String, Object> getResults(SearchRequest searchRequest) {
        List<Song> songs = songRepository.findByTitleContainingIgnoreCase(searchRequest.getInput());
        List<User> users = userRepository.findByNameContainingIgnoreCase(searchRequest.getInput());
        List<Playlist> playlists = playlistRepository.findByNameContainingIgnoreCase(searchRequest.getInput());

        Map<String, Object> searchResults = new HashMap<>();
        searchResults.put("songs", SongMapper.mapSongListToSongDTOList(songs));
        searchResults.put("users", UserMapper.mapUserListToUserDTOList(users));
        searchResults.put("playlists", PlaylistMapper.mapPlaylistListToDTOList(playlists));
        return searchResults;
    }
}
