package com.journal.adib.Journal.Service;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Library;
import com.journal.adib.Journal.repositories.LibraryRepository;
import com.journal.adib.Journal.services.LibraryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class LibraryServiceTest {

    @Mock
    LibraryRepository libraryRepository;

    @InjectMocks
    LibraryService libraryService;

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400(){
        when(libraryRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            libraryService.findById(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"Library not found");
            assertEquals(je.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void findById_Found_ShouldReturnFoundLanguageEntry() throws JournalException {
        Library l1 = new Library();
        l1.setId(new Long(3));
        l1.setName("Library1");
        Optional<Library> library = Optional.of(l1);
        when(libraryRepository.findById(1L)).thenReturn(library);

        assertEquals(libraryService.findById(1L), library.get());
    }

    @Test
    public void findAll_Found_ShouldReturnFoundLibraryEntries() throws JournalException {
        Library l1 = new Library();
        l1.setId(new Long(3));
        l1.setName("Library1");

        Library l2 = new Library();
        l2.setId(new Long(4));
        l2.setName("Library2");

        when(libraryRepository.findAll()).thenReturn(Arrays.asList(l1, l2));
        assertEquals(libraryService.findAll(), Arrays.asList(l1, l2));
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204(){

        when(libraryRepository.findAll()).thenReturn(new ArrayList<Library>());

        try{
            libraryService.findAll();
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"No libraries found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void save_Saved_ShouldReturnSavedLibrary() throws JournalException{
        Library l1 = new Library();
        l1.setId(new Long(3));
        l1.setName("Library1");

        when(libraryRepository.save(l1)).thenReturn(l1);
        assertEquals(libraryService.save(l1), l1);
    }

    @Test
    public void save_NotSaved_ShouldReturnHttpStatusCode500(){
        when(libraryRepository.save(new Library())).thenReturn(null);
        try{
            libraryService.save(new Library());
        }catch (JournalException je){
            assertEquals(je.getErrorMessage(), "Failed to save library");
            assertEquals(je.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
