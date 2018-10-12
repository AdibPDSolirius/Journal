package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.controllers.ResourceController;
import com.journal.adib.Journal.errorHandling.ErrorHandler;
import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.*;
import com.journal.adib.Journal.services.ResourceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceControllerTest {

    private String baseURL = "/api/resources/";


    private MockMvc mockMvc;

    @Mock
    ResourceService resourceServiceMock;

    @InjectMocks
    private ResourceController resourceController;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(resourceController)
                .setControllerAdvice(new ErrorHandler())
                .build();

    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode400() throws Exception {
        when(resourceServiceMock.findById(1L)).thenThrow(new JournalException("Resource not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "{resourceId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Resource not found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundResourceEntry() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        when(resourceServiceMock.findById(1L)).thenReturn(r1);

        mockMvc.perform(get(baseURL + "/{resourceId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Resource1")))
                .andExpect(jsonPath("$.url", is("Resource1Url")));

        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(resourceServiceMock.findAll()).thenThrow(new JournalException("No resources found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get(baseURL))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "No resources found");

        verify(resourceServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void findAll_Found_ShouldReturnFoundResourceEntries() throws Exception {
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


        when(resourceServiceMock.findAll()).thenReturn(Arrays.asList(r1, r2));


        mockMvc.perform(get(baseURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Resource1")))
                .andExpect(jsonPath("$[0].url", is("Resource1Url")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Resource2")))
                .andExpect(jsonPath("$[1].url", is("Resource2Url")))
                .andExpect(jsonPath("$[1].languages", hasSize(1)))
                .andExpect(jsonPath("$[1].languages[0].id", is(3)))
                .andExpect(jsonPath("$[1].languages[0].name", is("Language1")));

        verify(resourceServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void create_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Resource r1 = new Resource();
        r1.setName(TestUtil.createStringWithLength(31));
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));


        mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(resourceServiceMock);
    }

    @Test
    public void create_NotCreated_ShouldReturnHttpStatusCode500() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Hello");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        when(resourceServiceMock.save(any(Resource.class))).thenThrow(new JournalException("Failed to create new resource", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to create new resource", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).save(any(Resource.class));
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void create_Created_ShouldReturnCreatedResource() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        Set<Language> languageSet = new HashSet<>();
        languageSet.add(l1);

        r1.setLanguages(languageSet);

        when(resourceServiceMock.save(any(Resource.class))).thenReturn(r1);


        mockMvc.perform(post(baseURL)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Resource1")))
                .andExpect(jsonPath("$.url", is("Resource1Url")))
                .andExpect(jsonPath("$.languages", hasSize(1)))
                .andExpect(jsonPath("$.languages[0].id", is(3)))
                .andExpect(jsonPath("$.languages[0].name", is("Language1")));

        verify(resourceServiceMock, times(1)).save(any(Resource.class));
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void update_Updated_ShouldReturnUpdateResource() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Resource1");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        Language l1 = new Language();
        l1.setId(new Long(3));
        l1.setName("Language1");

        Set<Language> languageSet = new HashSet<>();
        languageSet.add(l1);

        r1.setLanguages(languageSet);

        when(resourceServiceMock.findById(1L)).thenReturn(r1);
        when(resourceServiceMock.save(any(Resource.class))).thenReturn(r1);


        mockMvc.perform(put(baseURL + "{resourceId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Resource1")))
                .andExpect(jsonPath("$.url", is("Resource1Url")))
                .andExpect(jsonPath("$.languages", hasSize(1)))
                .andExpect(jsonPath("$.languages[0].id", is(3)))
                .andExpect(jsonPath("$.languages[0].name", is("Language1")));

        verify(resourceServiceMock, times(1)).save(any(Resource.class));
        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void update_NotUpdated_ShouldReturnHttpStatusCode500() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Hello");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        when(resourceServiceMock.findById(1L)).thenReturn(r1);
        when(resourceServiceMock.save(any(Resource.class))).thenThrow(new JournalException("Failed to update new resource", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(put(baseURL + "{resourceId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to update new resource", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).save(any(Resource.class));
        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void update_IdNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        Resource r1 = new Resource();
        r1.setName("Hello");
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));

        when(resourceServiceMock.findById(1L)).thenThrow(new JournalException("Resource not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(put(baseURL+ "{resourceId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Resource not found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void update_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Resource r1 = new Resource();
        r1.setName(TestUtil.createStringWithLength(31));
        r1.setUrl("Resource1Url");
        r1.setId(new Long(1));


        mockMvc.perform(put(baseURL +"{resourceId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(r1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(resourceServiceMock);
    }

    @Test
    public void filterByLanguageId_Found_ShouldReturnFoundResourceEntries() throws Exception {
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


        when(resourceServiceMock.filterResourcesByLanguageId(3L)).thenReturn(Arrays.asList(r2));


        mockMvc.perform(get(baseURL + "language/{languageId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Resource2")))
                .andExpect(jsonPath("$[0].url", is("Resource2Url")))
                .andExpect(jsonPath("$[0].languages[0].id", is(3)))
                .andExpect(jsonPath("$[0].languages[0].name", is("Language1")));

        verify(resourceServiceMock, times(1)).filterResourcesByLanguageId(3L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByLanguageId_NotFound_ShouldReturnHttpStatusCode400() throws Exception {
        when(resourceServiceMock.filterResourcesByLanguageId(1L)).thenThrow(new JournalException("No resources found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "language/{languageId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No resources found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).filterResourcesByLanguageId(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByFrameworkId_Found_ShouldReturnFoundResourceEntries() throws Exception {
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

        Set<Framework> frameworkSet = new HashSet<>();
        frameworkSet.add(f1);

        r2.setFrameworks(frameworkSet);


        when(resourceServiceMock.filterResourcesByFrameworkId(3L)).thenReturn(Arrays.asList(r2));


        mockMvc.perform(get(baseURL + "framework/{frameworkId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Resource2")))
                .andExpect(jsonPath("$[0].url", is("Resource2Url")))
                .andExpect(jsonPath("$[0].frameworks[0].id", is(3)))
                .andExpect(jsonPath("$[0].frameworks[0].name", is("Framework1")));

        verify(resourceServiceMock, times(1)).filterResourcesByFrameworkId(3L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByFrameworkId_NotFound_ShouldReturnHttpStatusCode400() throws Exception {
        when(resourceServiceMock.filterResourcesByFrameworkId(1L)).thenThrow(new JournalException("No resources found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "framework/{frameworkId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No resources found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).filterResourcesByFrameworkId(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByLibraryId_Found_ShouldReturnFoundResourceEntries() throws Exception {
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

        Set<Library> librarySet = new HashSet<>();
        librarySet.add(l1);

        r2.setLibraries(librarySet);


        when(resourceServiceMock.filterResourcesByLibraryId(3L)).thenReturn(Arrays.asList(r2));


        mockMvc.perform(get(baseURL + "library/{libraryId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Resource2")))
                .andExpect(jsonPath("$[0].url", is("Resource2Url")))
                .andExpect(jsonPath("$[0].libraries[0].id", is(3)))
                .andExpect(jsonPath("$[0].libraries[0].name", is("Library1")));

        verify(resourceServiceMock, times(1)).filterResourcesByLibraryId(3L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByLibraryId_NotFound_ShouldReturnHttpStatusCode400() throws Exception {
        when(resourceServiceMock.filterResourcesByLibraryId(1L)).thenThrow(new JournalException("No resources found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "library/{libraryId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No resources found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).filterResourcesByLibraryId(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByDatabaseId_Found_ShouldReturnFoundResourceEntries() throws Exception {
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

        Set<Database> databaseSet = new HashSet<>();
        databaseSet.add(d1);

        r2.setDatabases(databaseSet);


        when(resourceServiceMock.filterResourcesByDatabaseId(3L)).thenReturn(Arrays.asList(r2));


        mockMvc.perform(get(baseURL + "database/{databaseId}", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Resource2")))
                .andExpect(jsonPath("$[0].url", is("Resource2Url")))
                .andExpect(jsonPath("$[0].databases[0].id", is(3)))
                .andExpect(jsonPath("$[0].databases[0].name", is("Database1")));

        verify(resourceServiceMock, times(1)).filterResourcesByDatabaseId(3L);
        verifyNoMoreInteractions(resourceServiceMock);
    }

    @Test
    public void filterByDatabaseId_NotFound_ShouldReturnHttpStatusCode400() throws Exception {
        when(resourceServiceMock.filterResourcesByDatabaseId(1L)).thenThrow(new JournalException("No resources found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get(baseURL + "database/{databaseId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No resources found", result.getResponse().getContentAsString());

        verify(resourceServiceMock, times(1)).filterResourcesByDatabaseId(1L);
        verifyNoMoreInteractions(resourceServiceMock);
    }


}