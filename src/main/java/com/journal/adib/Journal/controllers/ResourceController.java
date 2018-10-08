package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Resource;
import com.journal.adib.Journal.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/resources")
@RestController
public class ResourceController {

    @Autowired
    ResourceService resourceService;

    @GetMapping("")
    public ResponseEntity<List<Resource>>  findAll() throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findAll());

    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<Resource> findById(@PathVariable(value="resourceId") Long resoureceId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findById(resoureceId));
    }

    @GetMapping("/language/{languageId}")
    public ResponseEntity<List<Resource>> findByLanguageId(@PathVariable(value="languageId") Long languageId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByLanguageId(languageId));

    }

    @GetMapping("/framework/{frameworkId}")
    public ResponseEntity<List<Resource>> findByFrameworkId(@PathVariable(value="frameworkId") Long frameworkId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByFrameworkId(frameworkId));

    }
    @GetMapping("/database/{databaseId}")
    public ResponseEntity<List<Resource>> findByDatabaseId(@PathVariable(value="databaseId") Long databaseId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByDatabaseId(databaseId));

    }
    @GetMapping("/library/{libraryId}")
    public ResponseEntity<List<Resource>> findByLibraryId(@PathVariable(value="libraryId") Long libraryId) throws JournalException{
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.filterResourcesByLibraryId(libraryId));

    }

    @PostMapping("")
    public ResponseEntity<Resource> create(@Valid @RequestBody Resource resource) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceService.save(resource));
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<Resource> update(@PathVariable(value="resourceId") Long resourceId, @Valid  @RequestBody Resource inputResource) throws JournalException{
        Resource resource = resourceService.findById(resourceId);
        resource.setName(inputResource.getName());
        resource.setUrl(inputResource.getUrl());
        resource.setMemo(inputResource.getMemo());
        resource.setFile(inputResource.getFile());
        resource.setLanguages(inputResource.getLanguages());
        resource.setFrameworks(inputResource.getFrameworks());
        resource.setLibraries(inputResource.getLibraries());
        resource.setDatabases(inputResource.getDatabases());

        return ResponseEntity.status(HttpStatus.OK).body(resourceService.save(resource));
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Resource> deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteById(resourceId);
        return ResponseEntity.ok().build();
    }


}
