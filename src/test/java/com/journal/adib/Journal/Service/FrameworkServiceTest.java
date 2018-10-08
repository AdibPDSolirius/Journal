package com.journal.adib.Journal.Service;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Framework;
import com.journal.adib.Journal.repositories.FrameworkRepository;
import com.journal.adib.Journal.services.FrameworkService;
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
public class FrameworkServiceTest {

    @Mock
    FrameworkRepository frameworkRepository;

    @InjectMocks
    FrameworkService frameworkService;

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400(){
        when(frameworkRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            frameworkService.findById(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"Framework not found");
            assertEquals(je.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void findById_Found_ShouldReturnFoundFrameworkEntry() throws JournalException {
        Framework f1 = new Framework();
        f1.setId(new Long(3));
        f1.setName("Framework1");
        Optional<Framework> framework = Optional.of(f1);
        when(frameworkRepository.findById(1L)).thenReturn(framework);

        assertEquals(frameworkService.findById(1L), framework.get());
    }

    @Test
    public void findAll_Found_ShouldReturnFoundFrameworkEntries() throws JournalException{
        Framework f1 = new Framework();
        f1.setId(new Long(3));
        f1.setName("Framework1");

        Framework f2 = new Framework();
        f2.setId(new Long(4));
        f2.setName("Framework2");

        when(frameworkRepository.findAll()).thenReturn(Arrays.asList(f1, f2));
        assertEquals(frameworkService.findAll(), Arrays.asList(f1, f2));
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204(){

        when(frameworkRepository.findAll()).thenReturn(new ArrayList<Framework>());

        try{
            frameworkService.findAll();
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"No frameworks found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void save_Saved_ShouldReturnSavedFramework() throws JournalException{
        Framework f1 = new Framework();
        f1.setId(new Long(3));
        f1.setName("Framework1");

        when(frameworkRepository.save(f1)).thenReturn(f1);
        assertEquals(frameworkService.save(f1), f1);
    }

    @Test
    public void save_NotSaved_ShouldReturnHttpStatusCode500(){
        when(frameworkRepository.save(new Framework())).thenReturn(null);
        try{
            frameworkService.save(new Framework());
        }catch (JournalException je){
            assertEquals(je.getErrorMessage(), "Failed to save framework");
            assertEquals(je.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
