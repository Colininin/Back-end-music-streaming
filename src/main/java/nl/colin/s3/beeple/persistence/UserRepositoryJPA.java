package nl.colin.s3.beeple.persistence;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(@Param("email")String email);
    Optional<UserEntity> findByEmail(@Param("email")String email);
    List<UserEntity> findByNameContainingIgnoreCase(@Param("name")String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_entity u SET u.name = :name, u.email = :email WHERE u.id = :userId", nativeQuery = true)
    int editInfo(@Param("userId") Long userId, @Param("name") String name, @Param("email") String email);
}
