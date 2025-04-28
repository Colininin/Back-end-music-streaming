package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.business.exception.UserAlreadyExists;
import nl.colin.s3.beeple.persistence.UserRepository;
import nl.colin.s3.beeple.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        Long id = 1L;
        String name = "John";
        String email = "john@gmail.com";
        String password = "password";
        String role = "USER";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        User user = new User(id,name, email, password, role);
        when(userRepository.save(name, email, password)).thenReturn(user);

        User createdUser = userService.createUser(name, email, password);

        assertNotNull(createdUser);
        assertEquals(name, createdUser.getName());
        assertEquals(email, createdUser.getEmail());
        assertEquals(password, createdUser.getPassword());

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).save(name, email, password);
    }

    @Test
    void testCreateUser_AlreadyExists() {
        String name = "John";
        String email = "john@gmail.com";
        String password = "password";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> userService.createUser(name, email, password));

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, never()).save(anyString(), anyString(), anyString());
    }

    @Test
    void testGetUsersWithPagination() {
        User user1 = new User(1L, "John", "john@gmail.com", "password", "USER");
        User user2 = new User(2L, "Jane", "jane@gmail.com", "password123", "USER");

        Pageable pageable = PageRequest.of(0, 2);
        Page<User> mockPage = new PageImpl<>(Arrays.asList(user1, user2), pageable, 2);

        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        Page<User> result = userService.getUsers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getSize());
        assertTrue(result.getContent().contains(user1));
        assertTrue(result.getContent().contains(user2));

        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDeleteUser_success() {
        Long userId = 1L;
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteUserById(userId);
    }


    @Test
    void testGetUser_success() {
        String email = "john@gmail.com";
        User user = new User(1L, "John", email, "password", "USER");

        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.getUser(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("John", result.getName());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetUser_notFound() {
        String email = "geenIdee@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        User result = userService.getUser(email);

        assertNull(result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testEditInfo_success() {
        Long userId = 1L;
        String newName = "Updated Name";
        String newEmail = "updatedemail@gmail.com";

        User user = new User(1L, "John", "john@gmail.com", "password", "USER");

        when(userRepository.editUserIfo(userId, newName, newEmail)).thenReturn(true);

        ResponseEntity<Void> response = userService.editInfo(user, newName, newEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).editUserIfo(userId, newName, newEmail);
    }

    @Test
    void testEditInfo_failure() {
        Long userId = 1L;
        String newName = "Updated Name";
        String newEmail = "updatedemail@gmail.com";

        User user = new User(1L, "John", "john@gmail.com", "password", "USER");

        when(userRepository.editUserIfo(userId, newName, newEmail)).thenReturn(false);

        ResponseEntity<Void> response = userService.editInfo(user, newName, newEmail);

        assertEquals(404, response.getStatusCode().value());
        verify(userRepository, times(1)).editUserIfo(userId, newName, newEmail);
    }
}