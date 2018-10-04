package com.journal.adib.Journal.Service;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Repositories.LanguageRepository;
import com.journal.adib.Journal.Services.LanguageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class LanguageServiceTest {

    @Mock
    LanguageRepository languageRepository;

    @InjectMocks
    LanguageService languageService;

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400(){
        when(languageRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            languageService.findById(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"Language not found");
            assertEquals(je.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void findById_Found_ShouldReturnFoundLanguageEntry() throws JournalException {
        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");
        Optional<Language> language = Optional.of(l1);
        when(languageRepository.findById(1L)).thenReturn(language);

        assertEquals(languageService.findById(1L), language.get());
    }

    @Test
    public void findAll_Found_ShouldReturnFoundLanguageEntries() throws JournalException{
        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        Language l2 = new Language();
        l1.setId(new Long(4));
        l1.setName("Language1");

        when(languageRepository.findAll()).thenReturn(Arrays.asList(l1, l2));
        assertEquals(languageService.findAll(), Arrays.asList(l1, l2));
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204(){

        when(languageRepository.findAll()).thenReturn(new ArrayList<Language>());

        try{
            languageService.findAll();
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"No languages found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void save_Saved_ShouldReturnSavedLanguage() throws JournalException{
        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        when(languageRepository.save(l1)).thenReturn(l1);
        assertEquals(languageService.save(l1), l1);
    }

    @Test
    public void save_NotSaved_ShouldReturnHttpStatusCode500(){
        when(languageRepository.save(new Language())).thenReturn(null);
        try{
            languageService.save(new Language());
        }catch (JournalException je){
            assertEquals(je.getErrorMessage(), "Failed to save language");
            assertEquals(je.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
