package nl.colin.s3.beeple.business;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(String artist, MultipartFile file);
    ResponseEntity<Resource> returnSongFile(String songPath);
}
