package nl.colin.s3.beeple.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class GetUsersResponse {

    public GetUsersResponse() {};

    private List<UserDTO> users = new ArrayList<>();

    public List<UserDTO> getUsers()
    {
        return this.users;
    }

    public void setUsers(List<UserDTO> users){
        this.users = users;
    }
}
