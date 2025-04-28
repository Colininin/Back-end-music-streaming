package nl.colin.s3.beeple.controller;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.BeepleApplication;
import nl.colin.s3.beeple.business.UserService;
import nl.colin.s3.beeple.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BeepleApplication.class)
@Transactional
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    private UsersController usersController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void getUsers_success() throws Exception {
        User user = new User(1L, "freek", "test@gmail.com", "password", "USER");
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userService.getUsers(pageable)).thenReturn(mockPage);

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(1))
                .andExpect(jsonPath("$.users[0].name").value("freek"))
                .andExpect(jsonPath("$.users[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.pageSize").value(0));

        verify(userService, times(1)).getUsers(pageable);
    }

    @Test
    void getMe_success() throws Exception {
        User user = new User(1L, "Freek Vonk", "freek@gmail.com", "password", "USER");

        when(userService.getUser(user.getEmail())).thenReturn(user);

        mockMvc.perform(get("/users/user-info")
                        .requestAttr("email", user.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void getMe_userNotFound() throws Exception {
        String email = "iDOntExist@gmail.com";
        when(userService.getUser(email)).thenReturn(null);

        mockMvc.perform(get("/users/user-info")
                        .requestAttr("email", email))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_success() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/delete-user/{userId}", userId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteMe() throws Exception {
        String email = "freek@gmail.com";
        User user = new User(1L, "Freek Vonk", email, "password", "USER");

        when(userService.getUser(email)).thenReturn(user);
        doNothing().when(userService).deleteUser(user.getId());

        mockMvc.perform(delete("/users/delete-me")
                        .requestAttr("email", email))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void editMe_success() throws Exception {
        String currentUserEmail = "user@example.com";
        String newName = "Updated Name";
        String newEmail = "updated@example.com";

        User user = new User(1L, "Original Name", "user@example.com", "password", "USER");
        when(userService.getUser(currentUserEmail)).thenReturn(user);
        when(userService.editInfo(user, newName, newEmail)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/users/edit-my-info")
                        .requestAttr("email", currentUserEmail)
                        .contentType("application/json")
                        .content("{\"name\":\"" + newName + "\",\"email\":\"" + newEmail + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(userService, times(1)).getUser(currentUserEmail);
        verify(userService, times(1)).editInfo(user, newName, newEmail);
    }

    @Test
    void editMe_userNotFound() throws Exception {
        String currentUserEmail = "nonexistent@example.com";
        String newName = "Updated Name";
        String newEmail = "updated@example.com";

        when(userService.getUser(currentUserEmail)).thenReturn(null);

        mockMvc.perform(put("/users/edit-my-info")
                        .requestAttr("email", currentUserEmail)
                        .contentType("application/json")
                        .content("{\"name\":\"" + newName + "\",\"email\":\"" + newEmail + "\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(currentUserEmail);
        verify(userService, never()).editInfo(any(User.class), anyString(), anyString());
    }

}
