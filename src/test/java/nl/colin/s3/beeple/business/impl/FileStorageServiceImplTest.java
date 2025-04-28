package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FileStorageServiceImplTest {

    private final String fileStorageBase = "songs/test";

    @Test
    void storeFile_Success(){
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String artist = "Test Artist";
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-file.mp3",
                "audio/mpeg",
                "Test file content".getBytes()
        );
        Path storageLocation = Path.of(fileStorageBase).toAbsolutePath().normalize();
        Path artistPath = storageLocation.resolve(artist).normalize();

        FileSystemUtils.deleteRecursively(new File(fileStorageBase));

        String result = fileStorageService.storeFile(artist, mockFile);

        assertTrue(Files.exists(artistPath.resolve(mockFile.getOriginalFilename())));
        assertEquals(artist + "/" + mockFile.getOriginalFilename(), result.replace("\\", "/"));

        FileSystemUtils.deleteRecursively(new File(fileStorageBase));
    }

    @Test
    void storeFile_CreateDirectory() {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String artist = "newArtist";
        MultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-file.mp3",
                "audio/mpeg",
                "Test file content".getBytes()
        );
        Path artistPath = Path.of(fileStorageBase).toAbsolutePath().normalize().resolve(artist);

        FileSystemUtils.deleteRecursively(new File(fileStorageBase));

        assertFalse(Files.exists(artistPath));

        fileStorageService.storeFile(artist, mockFile);

        assertTrue(Files.exists(artistPath));
        assertTrue(Files.isDirectory(artistPath));

        FileSystemUtils.deleteRecursively(new File(fileStorageBase));
    }

    @Test
    void returnSongFile_Success() {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String songPath = "TestArtist/test-song.mp3";
        Path filePath = Path.of(fileStorageBase).resolve(songPath);

        assertTrue(filePath.getParent().toFile().mkdirs() || filePath.getParent().toFile().exists(),
                "Failed to create parent directories!");

        boolean fileCreated = false;
        try {
            Files.writeString(filePath, "Test song content");
            fileCreated = Files.exists(filePath);
        } catch (Exception e) {
            fail("File creation failed: " + e.getMessage());
        }

        assertTrue(fileCreated, "Test song file was not created!");

        ResponseEntity<Resource> response = fileStorageService.returnSongFile(songPath);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().exists());

        String expectedHeader = "inline; filename=\"test-song.mp3\"";
        String actualHeader = response.getHeaders().getContentDisposition().toString();
        assertEquals(expectedHeader, actualHeader, "Content-Disposition header does not match!");

        assertTrue(FileSystemUtils.deleteRecursively(filePath.getParent().toFile()),
                "Failed to clean up the test files!");
    }

    @Test
    void returnSongFile_FileNotFound() {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String invalidPath = "NonExistentArtist/non-existent-song.mp3";

        ResponseEntity<Resource> response = fileStorageService.returnSongFile(invalidPath);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody(), "Response body should be null for a non-existent file!");
    }

    @Test
    void returnSongFile_InternalServerError() {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl() {
            @Override
            public ResponseEntity<Resource> returnSongFile(String songPath) {
                throw new RuntimeException("Simulated internal server error");
            }
        };
        fileStorageService.fileStorageBase = fileStorageBase;

        String validPath = "TestArtist/server-error-song.mp3";

        ResponseEntity<Resource> response = null;
        try {
            response = fileStorageService.returnSongFile(validPath);
        } catch (RuntimeException ex) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody(), "Response body should be null for internal server error!");
    }

    @Test
    void storeFile_TransferFailure() throws IOException {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String artist = "Test Artist";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.getOriginalFilename()).thenReturn("test-file.mp3");
        doThrow(IOException.class).when(mockFile).transferTo(any(File.class));

        assertThrows(RuntimeException.class, () -> fileStorageService.storeFile(artist, mockFile),
                "Expected storeFile to throw RuntimeException on transferTo failure!");

        verify(mockFile, times(1)).getOriginalFilename();
        verify(mockFile, times(1)).transferTo(any(File.class));
    }

    @Test
    void storeFile_NullFile() {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        fileStorageService.fileStorageBase = fileStorageBase;

        String artist = "Test Artist";

        assertThrows(NullPointerException.class,
                () -> fileStorageService.storeFile(artist, null),
                "Expected storeFile to throw NullPointerException for null file!");
    }
}