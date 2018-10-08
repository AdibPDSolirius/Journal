package com.journal.adib.Journal;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"https://mysterious-woodland-86802.herokuapp.com", "http://localhost:4200"})
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @GetMapping("/libraries")
    public ResponseEntity<List<Library>>  findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.findAll());

    }

    @GetMapping("/libraries/{libraryId}")
    public ResponseEntity<Library> findById(@PathVariable(value="libraryId") Long libraryId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.findById(libraryId));
    }

    @PostMapping("/libraries")
    public ResponseEntity<Library> create(@Valid @RequestBody Library library) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryService.save(library));
    }

    @PutMapping("/libraries/{libraryId}")
    public ResponseEntity<Library> update(@PathVariable(value="libraryId") Long libraryId, @Valid @RequestBody Library inputLibrary) throws JournalException{
        Library library = libraryService.findById(libraryId);
        library.setName(inputLibrary.getName());
        return ResponseEntity.status(HttpStatus.OK).body(libraryService.save(library));
    }

    @DeleteMapping("/libraries/{libraryId}")
    public void deleteById(@PathVariable(value="libraryId") Long libraryId){
        libraryService.deleteById(libraryId);
    }


}
