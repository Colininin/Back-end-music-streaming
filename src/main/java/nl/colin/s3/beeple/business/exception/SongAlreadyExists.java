package nl.colin.s3.beeple.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SongAlreadyExists extends ResponseStatusException {
    public SongAlreadyExists() {
        super(HttpStatus.BAD_REQUEST, "Song already exists");
    }
}
