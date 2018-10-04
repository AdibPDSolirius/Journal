package com.journal.adib.Journal;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {

    @Autowired
    StorageService storageService;

    @PostMapping("/file/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file){
        try {
            String newFileName = storageService.store(file);
            return ResponseEntity.status(HttpStatus.OK).body(newFileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("FAIL to upload " + file.getOriginalFilename() + "!");
        }
    }

    @GetMapping(value = "file/{fileName}")
    public ResponseEntity<byte[]> handleFileRetrieval(@PathVariable(value="fileName") String fileName){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(storageService.getFile(fileName));
    }

}
