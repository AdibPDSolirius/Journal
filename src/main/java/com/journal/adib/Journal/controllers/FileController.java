package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.services.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/files")
@RestController
public class FileController {

    @Autowired
    StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(storageService.store(file));
    }

    @GetMapping("/{filename:.*}")
    public ResponseEntity<byte[]> handleFileRetrieval(@PathVariable(value="filename") String filename) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(storageService.getFile(filename));
    }

    @DeleteMapping("/{filename}")
    public void handleFileDeletion(@PathVariable(value="filename") String filename){
        storageService.deleteFile(filename);
    }

}
