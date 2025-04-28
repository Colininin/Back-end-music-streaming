package nl.colin.s3.beeple.persistence.impl;

import nl.colin.s3.beeple.domain.User;
import nl.colin.s3.beeple.business.exception.UserAlreadyExists;
import nl.colin.s3.beeple.persistence.UserRepository;
import nl.colin.s3.beeple.persistence.UserRepositoryJPA;
import nl.colin.s3.beeple.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJPA jpaRepo;

    public UserRepositoryImpl(UserRepositoryJPA jpaRepo){
        this.jpaRepo = jpaRepo;
    }

    @Override
    public boolean existsByEmail(String email){return jpaRepo.existsByEmail(email);}

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<UserEntity> users = jpaRepo.findAll(pageable);
        return users.map(this::convertToDomain);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = jpaRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToDomain(userEntity);
    }

    @Override
    public User save(String name, String email, String password) {
        UserEntity user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");

        try{
            UserEntity newUser = jpaRepo.save(user);
            return convertToDomain(newUser);
        }
        catch (Exception e){
            throw new UserAlreadyExists();
        }
    }

    @Override
    public List<User> findByNameContainingIgnoreCase(String name){
        List<UserEntity> users = jpaRepo.findByNameContainingIgnoreCase(name);
        return users.stream().map(this::convertToDomainForSearchResults).toList();
    }

    @Override
    public void deleteUserById(Long userId){
        String idString = userId.toString();
        jpaRepo.deleteById(idString);
    }

    @Override
    public boolean editUserIfo(Long userId, String name, String email){
        int rowsAffected = jpaRepo.editInfo(userId,name,email);
        return rowsAffected > 0;
    }


    private User convertToDomain(UserEntity userEntity){
        return new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
    }

    private User convertToDomainForSearchResults(UserEntity userEntity){
        return new User(userEntity.getId(), userEntity.getName());
    }
}
