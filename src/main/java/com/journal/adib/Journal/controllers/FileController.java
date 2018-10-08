package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.services.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api")
@RestController
public class FileUploadController {

    @Autowired
    StorageService storageService;

    @PostMapping("/file/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws JournalException{
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(!(extension.equals("jpeg") || extension.equals("png"))){
            throw new JournalException("Not correct file type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(storageService.store(file));
    }

    @GetMapping("file/{fileName}")
    public ResponseEntity<byte[]> handleFileRetrieval(@PathVariable(value="fileName") String fileName) throws JournalException{
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equals("jpeg")){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(storageService.getFile(fileName));
        }else if(extension.equals("png")){
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(storageService.getFile(fileName));
        }else{
            throw new JournalException("Not correct file type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @DeleteMapping("file/{fileName}")
    public ResponseEntity handleFileDeletion(@PathVariable(value="fileName") String fileName) throws JournalException{
        storageService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

}
