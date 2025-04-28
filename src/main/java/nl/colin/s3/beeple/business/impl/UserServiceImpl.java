package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.exception.UserAlreadyExists;
import nl.colin.s3.beeple.persistence.UserRepository;
import nl.colin.s3.beeple.business.UserService;
import nl.colin.s3.beeple.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(String name, String email, String password){
        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExists();
        }
        return userRepository.save(name, email, password);
    }

    @Override
    @Transactional
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> editInfo(User user, String name, String email){
        Long thisUserId = user.getId();

        if(userRepository.editUserIfo(thisUserId, name, email)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
