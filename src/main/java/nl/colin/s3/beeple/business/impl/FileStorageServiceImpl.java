package nl.colin.s3.beeple.business.impl;

import nl.colin.s3.beeple.business.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.storage.base}")
    String fileStorageBase;

    @Override
    public String storeFile(String artist, MultipartFile file) {
        try {

            Path storageLocation = Paths.get(fileStorageBase).toAbsolutePath().normalize();
            Path artistPath = storageLocation.resolve(artist).normalize();

            if (!Files.exists(artistPath)) {
                Files.createDirectories(artistPath);
            }

            Path filePath = artistPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath.toFile());
            String normalizedPath = filePath.toString().replace("\\", "/");
            return storageLocation.relativize(Paths.get(normalizedPath)).toString();

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public ResponseEntity<Resource> returnSongFile(String songPath) {
        Path filePath = Paths.get(fileStorageBase).resolve(songPath.replace("\\", "/")).normalize();

        try{
            Resource resource = new FileSystemResource(filePath);

            if(!resource.exists()){
                return ResponseEntity.notFound().build();
            }

            String contentType = "audio/mpeg";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
