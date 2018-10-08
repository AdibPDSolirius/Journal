package com.journal.adib.Journal.Service;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.*;
import com.journal.adib.Journal.repositories.ResourceRepository;
import com.journal.adib.Journal.services.*;
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
public class ResourceServiceTest {

    @Mock
    ResourceRepository resourceRepository;

    @Mock
    DatabaseService databaseService;

    @Mock
    FrameworkService frameworkService;

    @Mock
    LanguageService languageService;

    @Mock
    LibraryService libraryService;


    @InjectMocks
    ResourceService resourceService;

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400(){
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        try{
            resourceService.findById(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"Resource not found");
            assertEquals(je.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void findById_Found_ShouldReturnFoundResourceEntry() throws JournalException {
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));
        Optional<Resource> resource = Optional.of(r1);
        when(resourceRepository.findById(1L)).thenReturn(resource);

        assertEquals(resourceService.findById(1L), resource.get());
    }

    @Test
    public void findAll_Found_ShouldReturnFoundResourceEntries() throws JournalException{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Resource r2 = new Resource();
        r2.setName("Resource2");
        r2.setUrl("Resource2Url");
        r2.setId(new Long(2));

        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        Set<Language> languageSet = new HashSet<>();
        languageSet.add(l1);

        r2.setLanguages(languageSet);


        when(resourceRepository.findAll()).thenReturn(Arrays.asList(r1, r2));
        assertEquals(resourceService.findAll(), Arrays.asList(r1, r2));
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204(){

        when(resourceRepository.findAll()).thenReturn(new ArrayList<Resource>());

        try{
            resourceService.findAll();
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(),"No resources found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void save_Saved_ShouldReturnSavedResource() throws JournalException{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        when(resourceRepository.save(r1)).thenReturn(r1);
        assertEquals(resourceService.save(r1), r1);
    }

    @Test
    public void save_NotSaved_ShouldReturnHttpStatusCode500(){
        when(resourceRepository.save(new Resource())).thenReturn(null);
        try{
            resourceService.save(new Resource());
        }catch (JournalException je){
            assertEquals(je.getErrorMessage(), "Failed to save resource");
            assertEquals(je.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Test
    public void filterResourceByLanguageId_Found_ShouldReturnFilteredResources() throws Exception{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Resource r2 = new Resource();
        r2.setName("Resource2");
        r2.setUrl("Resource2Url");
        r2.setId(new Long(2));

        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        Set<Resource> resourceSet = new HashSet<>();
        resourceSet.add(r2);

        Set<Language> languageSet = new HashSet<>();
        languageSet.add(l1);

        r2.setLanguages(languageSet);
        l1.setResources(resourceSet);

        Set<Resource> resources = new HashSet<>();
        resources.add(r2);
        when(languageService.findById(l1.getId())).thenReturn(l1);

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(r2);
        assertEquals(resourceService.filterResourcesByLanguageId(l1.getId()), resourceList);
    }

    @Test
    public void filterResourceByLanguageId__NotFound_ShouldReturnHttpStatusCode404() throws Exception{
        when(languageService.findById(1L)).thenReturn(new Language());

        try{
            resourceService.filterResourcesByLanguageId(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(), "No resources found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void filterResourceByLibraryId_Found_ShouldReturnFilteredResources() throws Exception{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Resource r2 = new Resource();
        r2.setName("Resource2");
        r2.setUrl("Resource2Url");
        r2.setId(new Long(2));

        Library l1 = new Library();
        l1.setId(new Long(3));
        l1.setName("Library1");

        Set<Resource> resourceSet = new HashSet<>();
        resourceSet.add(r2);

        Set<Library> librarySet = new HashSet<>();
        librarySet.add(l1);

        r2.setLibraries(librarySet);
        l1.setResources(resourceSet);

        Set<Resource> resources = new HashSet<>();
        resources.add(r2);
        when(libraryService.findById(l1.getId())).thenReturn(l1);

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(r2);
        assertEquals(resourceService.filterResourcesByLibraryId(l1.getId()), resourceList);
    }

    @Test
    public void filterResourceByLibraryId__NotFound_ShouldReturnHttpStatusCode404() throws Exception{
        when(libraryService.findById(1L)).thenReturn(new Library());

        try{
            resourceService.filterResourcesByLibraryId(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(), "No resources found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void filterResourceByDatabaseId_Found_ShouldReturnFilteredResources() throws Exception{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Resource r2 = new Resource();
        r2.setName("Resource2");
        r2.setUrl("Resource2Url");
        r2.setId(new Long(2));

        Database d1 = new Database();
        d1.setId(new Long(3));
        d1.setName("Database1");

        Set<Resource> resourceSet = new HashSet<>();
        resourceSet.add(r2);

        Set<Database> databaseSet = new HashSet<>();
        databaseSet.add(d1);

        r2.setDatabases(databaseSet);
        d1.setResources(resourceSet);

        Set<Resource> resources = new HashSet<>();
        resources.add(r2);
        when(databaseService.findById(d1.getId())).thenReturn(d1);

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(r2);
        assertEquals(resourceService.filterResourcesByDatabaseId(d1.getId()), resourceList);
    }

    @Test
    public void filterResourceByDatabaseId__NotFound_ShouldReturnHttpStatusCode404() throws Exception{
        when(databaseService.findById(1L)).thenReturn(new Database());

        try{
            resourceService.filterResourcesByDatabaseId(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(), "No resources found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }

    @Test
    public void filterResourceByFrameworkId_Found_ShouldReturnFilteredResources() throws Exception{
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Resource r2 = new Resource();
        r2.setName("Resource2");
        r2.setUrl("Resource2Url");
        r2.setId(new Long(2));

        Framework f1 = new Framework();
        f1.setId(new Long(3));
        f1.setName("Framework1");

        Set<Resource> resourceSet = new HashSet<>();
        resourceSet.add(r2);

        Set<Framework> frameworkSet = new HashSet<>();
        frameworkSet.add(f1);

        r2.setFrameworks(frameworkSet);
        f1.setResources(resourceSet);

        Set<Resource> resources = new HashSet<>();
        resources.add(r2);
        when(frameworkService.findById(f1.getId())).thenReturn(f1);

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(r2);
        assertEquals(resourceService.filterResourcesByFrameworkId(f1.getId()), resourceList);
    }

    @Test
    public void filterResourceByFrameworkId__NotFound_ShouldReturnHttpStatusCode404() throws Exception{
        when(frameworkService.findById(1L)).thenReturn(new Framework());

        try{
            resourceService.filterResourcesByFrameworkId(1L);
        }catch(JournalException je){
            assertEquals(je.getErrorMessage(), "No resources found");
            assertEquals(je.getErrorCode(), HttpStatus.NO_CONTENT);
        }
    }
}
