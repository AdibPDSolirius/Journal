package com.journal.adib.Journal;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"https://mysterious-woodland-86802.herokuapp.com", "http://localhost:4200"})
public class FileUploadController {

    @Autowired
    StorageService storageService;

    @PostMapping("/file/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(storageService.store(file));
    }

    @GetMapping("file/{fileName}")
    public ResponseEntity<byte[]> handleFileRetrieval(@PathVariable(value="fileName") String fileName) throws JournalException{
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(storageService.getFile(fileName));
    }

    @DeleteMapping("file/{fileName}")
    public ResponseEntity handleFileDeletion(@PathVariable(value="fileName") String fileName) throws JournalException{
        storageService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

}
