package com.journal.adib.Journal.Controller;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.controllers.DatabaseController;
import com.journal.adib.Journal.errorHandling.ErrorHandler;
import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.*;
import com.journal.adib.Journal.services.DatabaseService;
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
public class DatabaseControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    DatabaseController databaseController;

    @Mock
    DatabaseService databaseServiceMock;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(databaseController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void findById_NotFound_ShouldReturnHttpStatusCode404() throws Exception {
        when(databaseServiceMock.findById(1L)).thenThrow(new JournalException("Database not found", HttpStatus.NOT_FOUND));

        MvcResult result = mockMvc.perform(get("/databases/{databaseId}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("Database not found", result.getResponse().getContentAsString());

        verify(databaseServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void findById_Found_ShouldReturnFoundDatabaseEntry() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));


        when(databaseServiceMock.findById(1L)).thenReturn(d1);

        mockMvc.perform(get("/databases/{databaseId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Database1")));

        verify(databaseServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void findAll_NotFound_ShouldReturnHttpStatusCode204() throws Exception {
        when(databaseServiceMock.findAll()).thenThrow(new JournalException("No databases found", HttpStatus.NO_CONTENT));

        MvcResult result = mockMvc.perform(get("/databases"))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), "No databases found");

        verify(databaseServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void findAll_Found_ShouldReturnFoundDatabaseEntries() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));

        Database d2 = new Database();
        d2.setName("Database2");
        d2.setId(new Long(2));


        when(databaseServiceMock.findAll()).thenReturn(Arrays.asList(d1, d2));


        mockMvc.perform(get("/databases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Database1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Database2")));

        verify(databaseServiceMock, times(1)).findAll();
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void create_Created_ShouldReturnCreatedDatabase() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));


        when(databaseServiceMock.save(any(Database.class))).thenReturn(d1);


        mockMvc.perform(post("/databases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Database1")));

        verify(databaseServiceMock, times(1)).save(any(Database.class));
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void create_NotCreated_ShouldReturnHttpStatusCode500() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));

        when(databaseServiceMock.save(any(Database.class))).thenThrow(new JournalException("Failed to create new database", HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult result = mockMvc.perform(post("/databases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to create new database", result.getResponse().getContentAsString());

        verify(databaseServiceMock, times(1)).save(any(Database.class));
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void create_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Database d1 = new Database();
        d1.setName(TestUtil.createStringWithLength(31));
        d1.setId(new Long(1));


        mockMvc.perform(post("/databases")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(databaseServiceMock);
    }

    @Test
    public void update_Updated_ShouldReturnUpdatedDatabase() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));

        when(databaseServiceMock.findById(1L)).thenReturn(d1);
        when(databaseServiceMock.save(any(Database.class))).thenReturn(d1);


        mockMvc.perform(put("/databases/{databaseId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Database1")));

        verify(databaseServiceMock, times(1)).findById(1L);
        verify(databaseServiceMock, times(1)).save(any(Database.class));
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void update_IdNotFound_ShouldReturnHttpStatusCode404() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));

        when(databaseServiceMock.findById(1L)).thenThrow(new JournalException("No database found", HttpStatus.NOT_FOUND));


        MvcResult result = mockMvc.perform(put("/databases/{databaseId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals("No database found", result.getResponse().getContentAsString());

        verify(databaseServiceMock, times(1)).findById(1L);
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void update_NotUpdated_ShouldReturnHttpStatusCode500() throws Exception {
        Database d1 = new Database();
        d1.setName("Database1");
        d1.setId(new Long(1));


        when(databaseServiceMock.findById(1L)).thenReturn(d1);
        when(databaseServiceMock.save(any(Database.class))).thenThrow(new JournalException("Failed to update new database", HttpStatus.INTERNAL_SERVER_ERROR));


        MvcResult result = mockMvc.perform(put("/databases/{databaseId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertEquals("Failed to update new database", result.getResponse().getContentAsString());

        verify(databaseServiceMock, times(1)).findById(1L);
        verify(databaseServiceMock, times(1)).save(any(Database.class));
        verifyNoMoreInteractions(databaseServiceMock);
    }

    @Test
    public void update_ValidationFailed_ShouldReturnHttpStatusCode400() throws Exception {
        Database d1 = new Database();
        d1.setName(TestUtil.createStringWithLength(31));
        d1.setId(new Long(1));


        mockMvc.perform(put("/databases/{databaseId}", 1L)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(d1)))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(databaseServiceMock);
    }
}
