package nl.colin.s3.beeple.persistence;

import nl.colin.s3.beeple.persistence.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ArtistRepositoryJPA extends JpaRepository<ArtistEntity, String> {
    boolean existsByName(@Param("name")String name);
    ArtistEntity findByNameContainingIgnoreCase(@Param("name")String name);
    ArtistEntity findById(@Param("id") Long id);
}
