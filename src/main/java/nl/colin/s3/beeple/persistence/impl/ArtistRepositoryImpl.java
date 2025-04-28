package nl.colin.s3.beeple.persistence.impl;

import nl.colin.s3.beeple.domain.Artist;
import nl.colin.s3.beeple.persistence.ArtistRepository;
import nl.colin.s3.beeple.persistence.ArtistRepositoryJPA;
import nl.colin.s3.beeple.persistence.entity.ArtistEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistRepositoryImpl implements ArtistRepository {

    private final ArtistRepositoryJPA jpaRepo;

    public ArtistRepositoryImpl(ArtistRepositoryJPA jpaRepo){
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Long getArtistId(String name) {
        ArtistEntity artist = jpaRepo.findByNameContainingIgnoreCase(name);
        if (artist == null) {
            throw new IllegalArgumentException("Artist not found with name: " + name);
        }
        return artist.getId();
    }

    @Override
    public boolean existsByName(String name){
        return jpaRepo.existsByName(name);
    }

    @Override
    public Artist save(String name) {
        if (existsByName(name)) {
            throw new IllegalArgumentException("Artist already exists with name: " + name);
        }

        ArtistEntity artist = new ArtistEntity();
        artist.setName(name);

        ArtistEntity newArtist = jpaRepo.save(artist);
        return convertToDomain(newArtist);
    }

    @Override
    public Artist findById(Long id){
        ArtistEntity artist = jpaRepo.findById(id);
        if (artist == null) {
            throw new IllegalArgumentException("Artist not found with id: " + id);
        }
        return convertToDomain(artist);
    }

    @Override
    public ArtistEntity findEntityById(Long id){
        ArtistEntity artist = jpaRepo.findById(id);
        if (artist == null) {
            throw new IllegalArgumentException("Artist not found with id: " + id);
        }
        return artist;
    }

    private Artist convertToDomain(ArtistEntity artistEntity){
        return new Artist(artistEntity.getId(), artistEntity.getName());
    }
}
