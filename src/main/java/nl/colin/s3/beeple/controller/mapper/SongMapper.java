package nl.colin.s3.beeple.controller.mapper;

import nl.colin.s3.beeple.controller.dto.GetSongsResponse;
import nl.colin.s3.beeple.controller.dto.SongDTO;
import nl.colin.s3.beeple.domain.Song;

import java.util.ArrayList;
import java.util.List;

public class SongMapper {
    public static GetSongsResponse mapSongListToGetSongResponse(List<Song> input){
        List<SongDTO> outputSongs = new ArrayList<>();

        for(Song song : input){
            outputSongs.add(mapSongToGetSongResponse(song));
        }

        GetSongsResponse response = new GetSongsResponse();
        response.setSongs(outputSongs);
        return response;
    }

    public static List<SongDTO> mapSongListToSongDTOList(List<Song> input){
        List<SongDTO> outputSongs = new ArrayList<>();
        for(Song song : input){
            outputSongs.add(mapSongToGetSongResponse(song));
        }
        return outputSongs;
    }

    public static SongDTO mapSongToGetSongResponse(Song input){
        SongDTO response = new SongDTO();
        response.setId(input.getId());
        response.setArtist(input.getArtist());
        response.setTitle(input.getTitle());
        response.setSongPath(input.getSongPath());

        return response;
    }
}
