package com.journal.adib.Journal.services;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.repositories.LanguageRepository;
import com.journal.adib.Journal.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> findAll() throws JournalException {
        List<Language> languageList = languageRepository.findAll();
        if(languageList.size() != 0){
            return languageList;
        }else{
            throw new JournalException("No languages found", HttpStatus.NO_CONTENT);
        }
    }

    public Language findById(Long id) throws JournalException {
        Optional<Language> ol = languageRepository.findById(id);
        if(ol.isPresent()){
            return ol.get();
        }else{
            throw new JournalException("Language not found", HttpStatus.NOT_FOUND);
        }
    }

    public Language save(Language language) throws JournalException{
        Language savedLanguage = languageRepository.save(language);
        if(savedLanguage != null){
            return savedLanguage;
        }else{
            throw new JournalException("Failed to save language", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteById(Long id) { languageRepository.deleteById(id); }

}
