package nl.colin.s3.beeple.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.colin.s3.beeple.business.*;
import nl.colin.s3.beeple.controller.dto.GetUserResponsePages;
import nl.colin.s3.beeple.controller.dto.UserDTO;
import nl.colin.s3.beeple.controller.mapper.UserMapper;
import nl.colin.s3.beeple.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    public UsersController(UserService userService)
    {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public GetUserResponsePages getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsers(pageable);
        return UserMapper.mapUserPageToResponse(users);
    }


    @GetMapping("/user-info")
    public ResponseEntity<UserDTO> getMe(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.getUser(email);

        if (user != null) {
            return ResponseEntity.ok(UserMapper.mapUserToGetUserResponse(user));
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-me")
    public ResponseEntity<Void> deleteMe(HttpServletRequest request){
        String email = (String) request.getAttribute("email");
        User user = userService.getUser(email);
        if(user != null) {
            userService.deleteUser(user.getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit-my-info")
    public ResponseEntity<Void> editMe(HttpServletRequest request, @RequestBody Map<String, String> payload){
        String currentUserEmail = (String) request.getAttribute("email");
        User thisUser = userService.getUser(currentUserEmail);

        if(thisUser != null) {
            String name = payload.get("name");
            String email = payload.get("email");
            return userService.editInfo(thisUser, name, email);
        }
        return ResponseEntity.notFound().build();

    }


}
