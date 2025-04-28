package nl.colin.s3.beeple.controller.mapper;


import nl.colin.s3.beeple.controller.dto.GetUsersResponse;
import nl.colin.s3.beeple.controller.dto.GetUserResponsePages;
import nl.colin.s3.beeple.controller.dto.UserDTO;
import nl.colin.s3.beeple.domain.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static GetUsersResponse mapUserListToGetUserResponse(List<User> input){

        List<UserDTO> outputUsers = new ArrayList<>();

        for(User user : input){
            outputUsers.add(mapUserToGetUserResponse(user));
        }

        GetUsersResponse response = new GetUsersResponse();
        response.setUsers(outputUsers);
        return response;
    }

    public static List<UserDTO> mapUserListToUserDTOList(List<User> input){
        List<UserDTO> outputUsers = new ArrayList<>();
        for(User user : input){
            outputUsers.add(mapUserToGetUserResponse(user));
        }
        return outputUsers;
    }

    public static UserDTO mapUserToGetUserResponse(User input){
        UserDTO response = new UserDTO();
        response.setId(input.getId());
        response.setName(input.getName());
        response.setEmail(input.getEmail());
        response.setRole(input.getRole());
        
        return response;
    }

    public static GetUserResponsePages mapUserPageToResponse(Page<User> usersPage){
        GetUserResponsePages response = new GetUserResponsePages();
        response.setUsers(usersPage.getContent());
        response.setTotalPages(usersPage.getTotalPages());
        response.setTotalElements(usersPage.getTotalElements());
        return response;
    }
}
