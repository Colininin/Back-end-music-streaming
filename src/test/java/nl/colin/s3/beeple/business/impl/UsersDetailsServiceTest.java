package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.persistence.UserRepository;
import nl.colin.s3.beeple.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest()
@Transactional
class UsersDetailsServiceTest {

    @Autowired
    private UsersDetailsService usersDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_Success() {
        String name = "John Deere";
        String email = "John@gmail.com";
        String password = "password123";
        String role = "USER";

        User mockUser = new User(1L, name, email, password, role);

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        var userDetails = usersDetailsService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(role, userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_fail() {
        String email = "JohnNotDeere@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            usersDetailsService.loadUserByUsername(email);
        });
    }
}