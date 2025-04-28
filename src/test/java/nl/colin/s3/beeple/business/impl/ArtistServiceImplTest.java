package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.ArtistService;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ArtistServiceImplTest {

    @Autowired
    private ArtistService artistService;

    @MockBean
    private ArtistRepository artistRepository;

    @Test
    void getArtistId_success() {
        String artistName = "Test Artist";
        Long expectedArtistId = 1L;

        when(artistRepository.getArtistId(artistName)).thenReturn(expectedArtistId);

        Long actualArtistId = artistService.getArtistId(artistName);

        assertNotNull(actualArtistId);
        assertEquals(expectedArtistId, actualArtistId);

        verify(artistRepository, times(1)).getArtistId(artistName);
    }

    @Test
    void createArtist_artistAlreadyExists() {
        String artistName = "Test Artist";
        Long artistId = 1L;
        Artist existingArtist = new Artist(artistId, artistName);

        when(artistRepository.existsByName(artistName)).thenReturn(true);
        when(artistRepository.getArtistId(artistName)).thenReturn(artistId);
        when(artistRepository.findById(artistId)).thenReturn(existingArtist);

        Artist result = artistService.createArtist(artistName);

        assertNotNull(result);
        assertEquals(existingArtist, result);

        verify(artistRepository, times(1)).existsByName(artistName);
        verify(artistRepository, times(1)).getArtistId(artistName);
        verify(artistRepository, times(1)).findById(artistId);
        verify(artistRepository, never()).save(anyString());
    }
}
