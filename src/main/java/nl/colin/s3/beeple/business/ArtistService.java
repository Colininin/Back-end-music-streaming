package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.domain.Artist;

public interface ArtistService {
    Long getArtistId(String name);
    Artist createArtist(String name);

}
