package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.LibraryController;
import com.journal.adib.Journal.ErrorHandling.ErrorHandler;
import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Services.LibraryService;
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
public class LibraryControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    LibraryController libraryController;

    @Mock
    LibraryService libraryServiceMock;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(libraryServiceMock.findById(1L)).thenThrow(new JournalException("Library not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get("/libraries/{libraryId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Library not found", result.getResponse().getContentAsString());

        verify(libraryServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundLibraryEntry() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));


        when(libraryServiceMock.findById(1L)).thenReturn(l1);

        mockMvc.perform(get("/libraries/{libraryId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Library1")));

        verify(libraryServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(libraryServiceMock.findAll()).thenThrow(new JournalException("No libraries found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get("/libraries"))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "No libraries found");

        verify(libraryServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void findAll_Found_ShouldReturnFoundLibraryEntries() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));

        Library l2 = new Library();
        l2.setName("Library2");
        l2.setId(new Long(2));


        when(libraryServiceMock.findAll()).thenReturn(Arrays.asList(l1, l2));


        mockMvc.perform(get("/libraries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Library1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Library2")));

        verify(libraryServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void create_Created_ShouldReturnCreatedLibrary() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));

        when(libraryServiceMock.save(any(Library.class))).thenReturn(l1);


        mockMvc.perform(post("/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Library1")));

        verify(libraryServiceMock, times(1)).save(any(Library.class));
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void create_NotCreated_ShouldReturnHttpStatusCode500() throws Exception {
        Library l1 = new Library();
        l1.setId(new Long(1));
        l1.setName("Library1");

        when(libraryServiceMock.save(any(Library.class))).thenThrow(new JournalException("Failed to create new library", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(post("/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to create new library", result.getResponse().getContentAsString());

        verify(libraryServiceMock, times(1)).save(any(Library.class));
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void create_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Library l1 = new Library();
        l1.setId(new Long(1));
        l1.setName(TestUtil.createStringWithLength(31));


        mockMvc.perform(post("/libraries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(libraryServiceMock);
    }

    @Test
    public void update_Updated_ShouldReturnUpdatedLibrary() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));

        when(libraryServiceMock.findById(1L)).thenReturn(l1);
        when(libraryServiceMock.save(any(Library.class))).thenReturn(l1);


        mockMvc.perform(put("/libraries/{libraryId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Library1")));

        verify(libraryServiceMock, times(1)).findById(1L);
        verify(libraryServiceMock, times(1)).save(any(Library.class));
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void update_IdNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));

        when(libraryServiceMock.findById(1L)).thenThrow(new JournalException("No library found", HttpStatus.NOT_FOUND));


        MvcResult result = mockMvc.perform(put("/libraries/{libraryId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No library found", result.getResponse().getContentAsString());

        verify(libraryServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void update_NotUpdated_ShouldReturnHttpStatusCode500() throws Exception {
        Library l1 = new Library();
        l1.setName("Library1");
        l1.setId(new Long(1));


        when(libraryServiceMock.findById(1L)).thenReturn(l1);
        when(libraryServiceMock.save(any(Library.class))).thenThrow(new JournalException("Failed to update new library", HttpStatus.INTERNAL_SERVER_ERROR));


        MvcResult result = mockMvc.perform(put("/libraries/{libraryId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to update new library", result.getResponse().getContentAsString());

        verify(libraryServiceMock, times(1)).findById(1L);
        verify(libraryServiceMock, times(1)).save(any(Library.class));
        verifyNoMoreInteractions(libraryServiceMock);
    }

    @Test
    public void update_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Library l1 = new Library();
        l1.setId(new Long(1));
        l1.setName(TestUtil.createStringWithLength(31));


        mockMvc.perform(put("/libraries/{libraryId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(l1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(libraryServiceMock);
    }
}
