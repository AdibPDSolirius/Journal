package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    @GetMapping("/resources")
    public ResponseEntity<List<Resource>>  findAll() throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findAll());

    }

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<Resource> findById(@PathVariable(value="resourceId") Long resoureceId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findById(resoureceId));
    }

    @GetMapping("/resources/language/{languageId}")
    public ResponseEntity<List<Resource>> findByLanguageId(@PathVariable(value="languageId") Long languageId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByLanguageId(languageId));

    }

    @GetMapping("/resources/framework/{frameworkId}")
    public ResponseEntity<List<Resource>> findByFrameworkId(@PathVariable(value="frameworkId") Long frameworkId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByFrameworkId(frameworkId));

    }
    @GetMapping("/resources/database/{databaseId}")
    public ResponseEntity<List<Resource>> findByDatabaseId(@PathVariable(value="databaseId") Long databaseId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByDatabaseId(databaseId));

    }
    @GetMapping("/resources/library/{libraryId}")
    public ResponseEntity<List<Resource>> findByLibraryId(@PathVariable(value="libraryId") Long libraryId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByLibraryId(libraryId));

    }

    @PostMapping("/resources")
    public ResponseEntity<Resource> create(@Valid @RequestBody Resource resource) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.save(resource));
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<Resource> deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteById(resourceId);
        return ResponseEntity.ok().build();
    }


}
