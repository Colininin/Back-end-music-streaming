package nl.colin.s3.beeple.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SongNotFound extends ResponseStatusException {
    public SongNotFound() {
        super(HttpStatus.BAD_REQUEST, "Song not found");
    }
}
