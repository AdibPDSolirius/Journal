package com.journal.adib.Journal.Service;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Database;
import com.journal.adib.Journal.repositories.DatabaseRepository;
import com.journal.adib.Journal.services.DatabaseService;
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
public class DatabaseServiceTest {

    @Mock
    DatabaseRepository databaseRepository;

    @InjectMocks
    DatabaseService databaseService;

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400(){
        when(databaseRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            databaseService.findById(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"Database not found");
            assertEquals(je.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void findById_Found_ShouldReturnFoundDatabaseEntry() throws JournalException {
        Database d1 = new Database();
        d1.setId(new Long(3));
        d1.setName("Database1");
        Optional<Database> database = Optional.of(d1);
        when(databaseRepository.findById(1L)).thenReturn(database);

        assertEquals(databaseService.findById(1L), database.get());
    }

    @Test
    public void findAll_Found_ShouldReturnFoundFrameworkEntries() throws JournalException{
        Database d1 = new Database();
        d1.setId(new Long(3));
        d1.setName("Database1");

        Database d2 = new Database();
        d2.setId(new Long(4));
        d2.setName("Database2");

        when(databaseRepository.findAll()).thenReturn(Arrays.asList(d1, d2));
        assertEquals(databaseService.findAll(), Arrays.asList(d1, d2));
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204(){

        when(databaseRepository.findAll()).thenReturn(new ArrayList<Database>());

        try{
            databaseService.findAll();
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"No databases found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void save_Saved_ShouldReturnSavedDatabase() throws JournalException{
        Database d1 = new Database();
        d1.setId(new Long(3));
        d1.setName("Database1");

        when(databaseRepository.save(d1)).thenReturn(d1);
        assertEquals(databaseService.save(d1), d1);
    }

    @Test
    public void save_NotSaved_ShouldReturnHttpStatusCode500(){
        when(databaseRepository.save(new Database())).thenReturn(null);
        try{
            databaseService.save(new Database());
        }catch (JournalException je){
            assertEquals(je.getErrorMessage(), "Failed to save database");
            assertEquals(je.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
