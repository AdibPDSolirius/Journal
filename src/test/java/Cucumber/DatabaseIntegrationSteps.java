package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.Database;
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

public class DatabaseIntegrationSteps {

    Database savedDatabase;
    HttpHeaders header;
    RestTemplate restTemplate;
    ParameterizedTypeReference<Database> singleDatabaseTypeRef;
    ParameterizedTypeReference<List<Database>> multipleDatabaseTypeRef;
    ResponseEntity<List<Database>> multipleDatabaseResponse;
    ResponseEntity<Database> singleDatabaseResponse;
    List<Database> savedDatabases;
    String databasePrefix;
    String databasesUrl;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        singleDatabaseTypeRef = new ParameterizedTypeReference<Database>() {};
        multipleDatabaseTypeRef = new ParameterizedTypeReference<List<Database>>() {};
        savedDatabases = new ArrayList<>();
        databasePrefix = "testDatabase";
        databasesUrl = "http://localhost:8080/api/databases/";

        cleanDatabases(restTemplate.exchange(databasesUrl, HttpMethod.GET, null, multipleDatabaseTypeRef));
    }

    void cleanDatabases(ResponseEntity<List<Database>> res){
        if(res == null){
            return;
        }
        if(res.getBody() == null){
            return;
        }
        for(Database d: res.getBody()){
            if(d.getName().contains(databasePrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(databasesUrl + d.getId(), HttpMethod.DELETE, null,
                        new ParameterizedTypeReference<Database>() {
                        });
            }
        }
    }

    void cleanDatabase(ResponseEntity<Database> res){
        if(res == null){
            return;
        }
        Database r = res.getBody();
        if(r.getName().contains(databasePrefix)){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(databasesUrl + r.getId(), HttpMethod.DELETE, null, singleDatabaseTypeRef);
        }
    }

    @Given("^test database objects posted")
    public void test_database_objects_posted() throws IOException {
        for(int i = 0; i < 3; i++){
            Database r = new Database();
            r.setName(databasePrefix + i);

            HttpEntity<Database> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Database> response = restTemplate.exchange(databasesUrl, HttpMethod.POST, request, singleDatabaseTypeRef);
            savedDatabases.add(response.getBody());
        }
    }


    @When("^the client calls /api/databases")
    public void the_client_issues_GET_ALL_DATABASES_REQUEST(){
        multipleDatabaseResponse = restTemplate.exchange(databasesUrl, HttpMethod.GET, null, multipleDatabaseTypeRef);
    }

    @Then("^the client receives database status code of (\\d+)$")
    public void the_client_receives_database_status_code_of(int statusCode){
        if(singleDatabaseResponse != null){
            assertEquals(statusCode, singleDatabaseResponse.getStatusCode().value());
        }else if(multipleDatabaseResponse != null){
            assertEquals(statusCode, multipleDatabaseResponse.getStatusCode().value());
        }
    }

    @And("^the client receives the databases")
    public void the_client_receives_the_databases(){
        int size = 0;
        for(Database d: multipleDatabaseResponse.getBody()){
            if(d.getName().contains(databasePrefix)){
                size++;
            }
        }
        assertEquals(savedDatabases.size(), size);
        cleanDatabases(multipleDatabaseResponse);
    }

    @Given("^single test database object posted")
    public void single_test_database_object_posted() throws IOException{
        Database d = new Database();
        d.setName(databasePrefix + "0");
        HttpEntity<Database> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(d), header);
        ResponseEntity<Database> databaseResponse = restTemplate.exchange(databasesUrl, HttpMethod.POST, request, singleDatabaseTypeRef);
        savedDatabase = databaseResponse.getBody();
    }

    @When("^the client makes a GET database request$")
    public void the_client_issues_GET_BY_ID_REQUEST(){
        singleDatabaseResponse = restTemplate.exchange(databasesUrl + savedDatabase.getId(), HttpMethod.GET, null, singleDatabaseTypeRef);
    }

    @And("^the client receives the correct database")
    public void the_client_receives_the_correct_database(){
        Database d = singleDatabaseResponse.getBody();
        assertEquals(savedDatabase.getId(), d.getId());
        assertEquals(savedDatabase.getName(), d.getName());
        cleanDatabase(singleDatabaseResponse);
    }

    @When("^the client makes a POST database request")
    public void the_client_makes_a_POST_database_request() throws IOException{
        Database d = new Database();
        d.setName(databasePrefix + "0");
        savedDatabase = d;
        HttpEntity<Database> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(d), header);
        singleDatabaseResponse = restTemplate.exchange(databasesUrl, HttpMethod.POST, request, singleDatabaseTypeRef);
    }

    @And("^the database is added to the database")
    public void the_database_is_added_to_the_database(){
        ResponseEntity<Database> databaseResponse = restTemplate.exchange(databasesUrl + singleDatabaseResponse.getBody().getId(), HttpMethod.GET, null, singleDatabaseTypeRef);
        assertFalse(databaseResponse == null);
    }

    @And("^the client receives the correct added database")
    public void the_client_receives_the_correct_added_database(){
        Database d = singleDatabaseResponse.getBody();
        assertEquals(savedDatabase.getName(), d.getName());
        cleanDatabase(singleDatabaseResponse);
    }

    @When("^the client makes a PUT database request with a new database")
    public void the_client_makes_a_PUT_database_request_with_a_new_database() throws IOException{
        Database d = new Database();
        d.setName(databasePrefix + "1");
        savedDatabase.setName(d.getName());
        HttpEntity<Database> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(d), header);
        singleDatabaseResponse = restTemplate.exchange(databasesUrl + savedDatabase.getId(), HttpMethod.PUT, request, singleDatabaseTypeRef);
        savedDatabase = singleDatabaseResponse.getBody();
    }

    @And("^the database is updated in the database")
    public void the_resource_is_updated_in_the_database(){
        ResponseEntity<Database> databaseResponse = restTemplate.exchange(databasesUrl + savedDatabase.getId(), HttpMethod.GET, null, singleDatabaseTypeRef);
        assertEquals(savedDatabase.getName(), databaseResponse.getBody().getName());
    }
}
