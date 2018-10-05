package com.journal.adib.Journal;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "https://mysterious-woodland-86802.herokuapp.com")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @GetMapping("/languages")
    public ResponseEntity<List<Language>>  findAll() throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findAll());

    }

    @GetMapping("/languages/{languageId}")
    public ResponseEntity<Language> findById(@PathVariable(value="languageId") Long languageId) throws JournalException {
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findById(languageId));
    }

    @PostMapping("/languages")
    public ResponseEntity<Language> create(@Valid @RequestBody Language language) throws JournalException{
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.save(language));
    }

    @PutMapping("/languages/{languageId}")
    public ResponseEntity<Language> update(@PathVariable(value="languageId") Long languageId, @Valid  @RequestBody Language inputLanguage) throws JournalException{
        Language language = languageService.findById(languageId);
        language.setName(inputLanguage.getName());
        return ResponseEntity.status(HttpStatus.OK).body(languageService.save(language));
    }

    @DeleteMapping("/languages/{languageId}")
    public void deleteById(@PathVariable(value="languageId") Long languageId){
        languageService.deleteById(languageId);
    }


}
