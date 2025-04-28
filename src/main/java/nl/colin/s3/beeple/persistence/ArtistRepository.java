package nl.colin.s3.beeple.persistence;

import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.entity.ArtistEntity;

public interface ArtistRepository {
    Long getArtistId(String name);
    boolean existsByName(String name);
    Artist save(String name);
    Artist findById(Long id);
    ArtistEntity findEntityById(Long id);
}
