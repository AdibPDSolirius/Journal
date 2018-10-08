package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Library;
import com.journal.adib.Journal.services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/libraries")
@RestController
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @GetMapping("")
    public ResponseEntity<List<Library>>  findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.findAll());

    }

    @GetMapping("/{libraryId}")
    public ResponseEntity<Library> findById(@PathVariable(value="libraryId") Long libraryId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.findById(libraryId));
    }

    @PostMapping("")
    public ResponseEntity<Library> create(@Valid @RequestBody Library library) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.save(library));
    }

    @PutMapping("/{libraryId}")
    public ResponseEntity<Library> update(@PathVariable(value="libraryId") Long libraryId, @Valid @RequestBody Library inputLibrary) throws JournalException{
        Library library = libraryService.findById(libraryId);
        library.setName(inputLibrary.getName());
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.save(library));
    }

    @DeleteMapping("/{libraryId}")
    public void deleteById(@PathVariable(value="libraryId") Long libraryId){
        libraryService.deleteById(libraryId);
    }


}
