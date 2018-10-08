package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Database;
import com.journal.adib.Journal.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/databases")
@RestController
public class DatabaseController {

    @Autowired
    DatabaseService databaseService;

    @GetMapping("")
    public ResponseEntity<List<Database>> findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(databaseService.findAll());

    }

    @GetMapping("/{databaseId}")
    public ResponseEntity<Database> findById(@PathVariable(value="databaseId") Long databaseId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(databaseService.findById(databaseId));
    }

    @PostMapping("")
    public ResponseEntity<Database> create(@Valid @RequestBody Database database) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(databaseService.save(database));
    }

    @PutMapping("/{databaseId}")
    public ResponseEntity<Database> update(@PathVariable(value="databaseId") Long databaseId, @Valid @RequestBody Database inputDatabase) throws JournalException{
        Database database = databaseService.findById(databaseId);
        database.setName(inputDatabase.getName());
        return ResponseEntity.status(HttpStatus.OK).body(databaseService.save(database));
    }

    @DeleteMapping("/{databaseId}")
    public void deleteById(@PathVariable(value="databaseId") Long databaseId){
        databaseService.deleteById(databaseId);
    }

}
