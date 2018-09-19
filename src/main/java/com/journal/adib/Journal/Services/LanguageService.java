package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.Repositories.LanguageRepository;
import com.journal.adib.Journal.Models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    public List<Language> findAll(){
        return (List<Language>) languageRepository.findAll();
    }

    public Language saveLanguage(Language language){
        return languageRepository.save(language);
    }

    public void deleteLanguage(Long id) { languageRepository.deleteById(id); }
}
