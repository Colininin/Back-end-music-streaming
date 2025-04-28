package nl.colin.s3.beeple.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExists extends ResponseStatusException {
    public UserAlreadyExists() {
        super(HttpStatus.BAD_REQUEST, "User already exists");
    }
}
