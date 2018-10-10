package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.Library;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LibraryIntegrationSteps {
    Library savedLibrary;
    HttpHeaders header;
    RestTemplate restTemplate;
    ParameterizedTypeReference<Library> singleLibraryTypeRef;
    ParameterizedTypeReference<List<Library>> multipleLibraryTypeRef;
    ResponseEntity<List<Library>> multipleLibraryResponse;
    ResponseEntity<Library> singleLibraryResponse;
    List<Library> savedLibraries;
    String libraryPrefix;
    String librariesUrl;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        singleLibraryTypeRef = new ParameterizedTypeReference<Library>() {};
        multipleLibraryTypeRef = new ParameterizedTypeReference<List<Library>>() {};
        savedLibraries = new ArrayList<>();
        libraryPrefix = "testLibrary";
        librariesUrl = "http://localhost:8080/api/libraries/";

        cleanLibraries(restTemplate.exchange(librariesUrl, HttpMethod.GET, null, multipleLibraryTypeRef));
    }

    void cleanLibraries(ResponseEntity<List<Library>> res){
        if(res == null){
            return;
        }
        if(res.getBody() == null){
            return;
        }
        for(Library l: res.getBody()){
            if(l.getName().contains(libraryPrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(librariesUrl + l.getId(), HttpMethod.DELETE, null,
                        new ParameterizedTypeReference<Library>() {
                        });
            }
        }
    }

    void cleanLibrary(ResponseEntity<Library> res){
        if(res == null){
            return;
        }
        Library l = res.getBody();
        if(l.getName().contains(libraryPrefix)){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(librariesUrl + l.getId(), HttpMethod.DELETE, null, singleLibraryTypeRef);
        }
    }

    @Given("^test library objects posted")
    public void test_library_objects_posted() throws IOException {
        for(int i = 0; i < 3; i++){
            Library l = new Library();
            l.setName(libraryPrefix + i);

            HttpEntity<Library> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
            ResponseEntity<Library> response = restTemplate.exchange(librariesUrl, HttpMethod.POST, request, singleLibraryTypeRef);
            savedLibraries.add(response.getBody());
        }
    }


    @When("^the client calls /api/libraries")
    public void the_client_issues_GET_ALL_LIBRARIES_REQUEST(){
        multipleLibraryResponse = restTemplate.exchange(librariesUrl, HttpMethod.GET, null, multipleLibraryTypeRef);
    }

    @Then("^the client receives library status code of (\\d+)$")
    public void the_client_receives_library_status_code_of(int statusCode){
        if(singleLibraryResponse != null){
            assertEquals(statusCode, singleLibraryResponse.getStatusCode().value());
        }else if(multipleLibraryResponse != null){
            assertEquals(statusCode, multipleLibraryResponse.getStatusCode().value());
        }
    }

    @And("^the client receives the libraries")
    public void the_client_receives_the_libraries(){
        int size = 0;
        for(Library l: multipleLibraryResponse.getBody()){
            if(l.getName().contains(libraryPrefix)){
                size++;
            }
        }
        assertEquals(savedLibraries.size(), size);
        cleanLibraries(multipleLibraryResponse);
    }

    @Given("^single test library object posted")
    public void single_test_library_object_posted() throws IOException{
        Library l = new Library();
        l.setName(libraryPrefix + "0");
        HttpEntity<Library> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        ResponseEntity<Library> libraryResponse= restTemplate.exchange(librariesUrl, HttpMethod.POST, request, singleLibraryTypeRef);
        savedLibrary = libraryResponse.getBody();
    }

    @When("^the client makes a GET library request$")
    public void the_client_issues_GET_BY_ID_REQUEST(){
        singleLibraryResponse = restTemplate.exchange(librariesUrl + savedLibrary.getId(), HttpMethod.GET, null, singleLibraryTypeRef);
    }

    @And("^the client receives the correct library")
    public void the_client_receives_the_correct_library(){
        Library l = singleLibraryResponse.getBody();
        assertEquals(savedLibrary.getId(), l.getId());
        assertEquals(savedLibrary.getName(), l.getName());
        cleanLibrary(singleLibraryResponse);
    }

    @When("^the client makes a POST library request")
    public void the_client_makes_a_POST_library_request() throws IOException{
        Library l = new Library();
        l.setName(libraryPrefix + "0");
        savedLibrary = l;
        HttpEntity<Library> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        singleLibraryResponse = restTemplate.exchange(librariesUrl, HttpMethod.POST, request, singleLibraryTypeRef);
    }

    @And("^the library is added to the database")
    public void the_library_is_added_to_the_database(){
        ResponseEntity<Library> libraryResponse = restTemplate.exchange(librariesUrl + singleLibraryResponse.getBody().getId(), HttpMethod.GET, null, singleLibraryTypeRef);
        assertFalse(libraryResponse == null);
    }

    @And("^the client receives the correct added library")
    public void the_client_receives_the_correct_added_library(){
        Library l = singleLibraryResponse.getBody();
        assertEquals(savedLibrary.getName(), l.getName());
        cleanLibrary(singleLibraryResponse);
    }

    @When("^the client makes a PUT library request with a new library")
    public void the_client_makes_a_PUT_library_request_with_a_new_library() throws IOException{
        Library l = new Library();
        l.setName(libraryPrefix + "1");
        savedLibrary.setName(l.getName());
        HttpEntity<Library> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        singleLibraryResponse = restTemplate.exchange(librariesUrl + savedLibrary.getId(), HttpMethod.PUT, request, singleLibraryTypeRef);
        savedLibrary = singleLibraryResponse.getBody();
    }

    @And("^the library is updated in the database")
    public void the_library_is_updated_in_the_database(){
        ResponseEntity<Library> libraryResponse = restTemplate.exchange(librariesUrl + savedLibrary.getId(), HttpMethod.GET, null, singleLibraryTypeRef);
        assertEquals(savedLibrary.getName(), libraryResponse.getBody().getName());
    }
}
