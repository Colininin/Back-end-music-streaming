package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.ArtistService;
import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.ArtistRepository;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional
    public Long getArtistId(String name){
        return artistRepository.getArtistId(name);
    }

    @Override
    @Transactional
    public Artist createArtist(String name) {
        if (artistRepository.existsByName(name)) {
            return artistRepository.findById(artistRepository.getArtistId(name));
        }
        return artistRepository.save(name);
    }

}
