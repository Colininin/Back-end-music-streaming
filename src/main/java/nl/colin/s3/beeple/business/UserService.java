package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface UserService {
    User createUser(String name, String email, String password);
    User getUser(String email);
    Page<User> getUsers(Pageable pageable);
    void deleteUser(Long userId);
    ResponseEntity<Void> editInfo(User user, String name, String Email);
}
