package nl.colin.s3.beeple.persistence;

import nl.colin.s3.beeple.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepository {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    User save(String name, String email, String password);
    List<User> findByNameContainingIgnoreCase(String name);
    void deleteUserById(Long userId);
    boolean editUserIfo(Long userId, String name, String email);
}
