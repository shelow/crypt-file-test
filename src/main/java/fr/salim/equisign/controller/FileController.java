package fr.salim.equisign.controller;

import fr.salim.equisign.domain.entities.CustomFile;
import fr.salim.equisign.domain.exceptions.*;
import fr.salim.equisign.domain.usecases.DownloadFile;
import fr.salim.equisign.domain.usecases.UploadFile;
import fr.salim.equisign.domain.values.CryptoParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    public static final String ATTACHMENT_FILENAME = "attachment; filename=\"";
    public static final String END = "\"";

    @Autowired
    UploadFile uploadFile;
    @Autowired
    DownloadFile downloadFile;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam MultipartFile file, @RequestParam String password){
        try {
            LOGGER.info("début de l'upload du fichier " + file.getOriginalFilename());
            uploadFile(file, password);
            LOGGER.info("fin de l'upload du fichier " + file.getOriginalFilename());
            return ResponseEntity.accepted().build();
        } catch (Exception exception) {
            LOGGER.error("Erreur lors de l'upload de fichier", exception);
            throw new RuntimeException(exception);
        }
    }

    private synchronized void uploadFile(MultipartFile file, String password) throws Exception {
        uploadFile.handle(
                new CustomFile(
                        file.getOriginalFilename(),
                        file.getInputStream().readAllBytes()),
                CryptoParams.of(true, password));
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName,  String password){
        try {
            LOGGER.info("Téléchargement du fichier " + fileName );
            CustomFile fileFound = downloadFile.handle(fileName, password);
            return responseWithHeader(fileFound, new ByteArrayResource(fileFound.content));
        } catch (Exception exception) {
            LOGGER.error("Erreur lors du téléchargement du fichier " + fileName, exception);
            throw new RuntimeException(exception);
        }
    }

    private ResponseEntity<ByteArrayResource> responseWithHeader(CustomFile file, ByteArrayResource resource) {
        String headerValue = ATTACHMENT_FILENAME + file.name + END;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,headerValue)
                .body(resource);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException exc) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(DuplicateFileNameException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateFileNameException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<?> handleEmptyFile(EmptyFileException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(MissingFileException.class)
    public ResponseEntity<?> handleMissingFile(EmptyFileException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
