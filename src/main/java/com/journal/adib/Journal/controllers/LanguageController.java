package com.journal.adib.Journal.controllers;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Language;
import com.journal.adib.Journal.services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/languages")
@RestController
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @GetMapping("")
    public ResponseEntity<List<Language>>  findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findAll());

    }

    @GetMapping("/{languageId}")
    public ResponseEntity<Language> findById(@PathVariable(value="languageId") Long languageId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findById(languageId));
    }

    @PostMapping("")
    public ResponseEntity<Language> create(@Valid @RequestBody Language language) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.save(language));
    }

    @PutMapping("/{languageId}")
    public ResponseEntity<Language> update(@PathVariable(value="languageId") Long languageId, @Valid  @RequestBody Language inputLanguage) throws JournalException{
        Language language = languageService.findById(languageId);
        language.setName(inputLanguage.getName());
        return ResponseEntity.status(HttpStatus.OK).body(languageService.save(language));
    }

    @DeleteMapping("/{languageId}")
    public void deleteById(@PathVariable(value="languageId") Long languageId){
        languageService.deleteById(languageId);
    }


}
