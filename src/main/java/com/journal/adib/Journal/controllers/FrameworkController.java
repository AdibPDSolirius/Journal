package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Framework;
import com.journal.adib.Journal.services.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = {"https://mysterious-woodland-86802.herokuapp.com", "http://localhost:4200"})
public class FrameworkController {

    @Autowired
    FrameworkService frameworkService;

    @GetMapping("/frameworks")
    public ResponseEntity<List<Framework>> findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(frameworkService.findAll());

    }

    @GetMapping("/frameworks/{frameworkId}")
    public ResponseEntity<Framework> findById(@PathVariable(value="frameworkId") Long frameworkId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(frameworkService.findById(frameworkId));
    }

    @PostMapping("/frameworks")
    public ResponseEntity<Framework> create(@Valid @RequestBody Framework framework) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(frameworkService.save(framework));
    }

    @PutMapping("/frameworks/{frameworkId}")
    public ResponseEntity<Framework> update(@PathVariable(value="frameworkId") Long frameworkId, @Valid @RequestBody Framework inputFramework) throws JournalException{
        Framework framework = frameworkService.findById(frameworkId);
        framework.setName(inputFramework.getName());
        return ResponseEntity.status(HttpStatus.OK).body(frameworkService.save(framework));
    }

    @DeleteMapping("/frameworks/{frameworkId}")
    public void deleteById(@PathVariable(value="frameworkId") Long frameworkId){
        frameworkService.deleteById(frameworkId);
    }

}
