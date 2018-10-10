package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.Framework;
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

public class FrameworkIntegrationSteps {
    Framework savedFramework;
    HttpHeaders header;
    RestTemplate restTemplate;
    ParameterizedTypeReference<Framework> singleFrameworkTypeRef;
    ParameterizedTypeReference<List<Framework>> multipleFrameworkTypeRef;
    ResponseEntity<List<Framework>> multipleFrameworkResponse;
    ResponseEntity<Framework> singleFrameworkResponse;
    List<Framework> savedFrameworks;
    String frameworkPrefix;
    String frameworksUrl;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        singleFrameworkTypeRef = new ParameterizedTypeReference<Framework>() {};
        multipleFrameworkTypeRef = new ParameterizedTypeReference<List<Framework>>() {};
        savedFrameworks = new ArrayList<>();
        frameworkPrefix = "testFramework";
        frameworksUrl = "http://localhost:8080/api/frameworks/";

        cleanFrameworks(restTemplate.exchange(frameworksUrl, HttpMethod.GET, null, multipleFrameworkTypeRef));
    }

    void cleanFrameworks(ResponseEntity<List<Framework>> res){
        if(res == null){
            return;
        }
        if(res.getBody() == null){
            return;
        }
        for(Framework f: res.getBody()){
            if(f.getName().contains(frameworkPrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(frameworksUrl + f.getId(), HttpMethod.DELETE, null,
                        new ParameterizedTypeReference<Framework>() {
                        });
            }
        }
    }

    void cleanFramework(ResponseEntity<Framework> res){
        if(res == null){
            return;
        }
        Framework f = res.getBody();
        if(f.getName().contains(frameworkPrefix)){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(frameworksUrl + f.getId(), HttpMethod.DELETE, null, singleFrameworkTypeRef);
        }
    }

    @Given("^test framework objects posted")
    public void test_framework_objects_posted() throws IOException {
        for(int i = 0; i < 3; i++){
            Framework f = new Framework();
            f.setName(frameworkPrefix + i);

            HttpEntity<Framework> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(f), header);
            ResponseEntity<Framework> response = restTemplate.exchange(frameworksUrl, HttpMethod.POST, request, singleFrameworkTypeRef);
            savedFrameworks.add(response.getBody());
        }
    }


    @When("^the client calls /api/frameworks")
    public void the_client_issues_GET_ALL_FRAMEWORKS_REQUEST(){
        multipleFrameworkResponse = restTemplate.exchange(frameworksUrl, HttpMethod.GET, null, multipleFrameworkTypeRef);
    }

    @Then("^the client receives framework status code of (\\d+)$")
    public void the_client_receives_framework_status_code_of(int statusCode){
        if(singleFrameworkResponse != null){
            assertEquals(statusCode, singleFrameworkResponse.getStatusCode().value());
        }else if(multipleFrameworkResponse != null){
            assertEquals(statusCode, multipleFrameworkResponse.getStatusCode().value());
        }
    }

    @And("^the client receives the frameworks")
    public void the_client_receives_the_frameworks(){
        int size = 0;
        for(Framework f: multipleFrameworkResponse.getBody()){
            if(f.getName().contains(frameworkPrefix)){
                size++;
            }
        }
        assertEquals(savedFrameworks.size(), size);
        cleanFrameworks(multipleFrameworkResponse);
    }

    @Given("^single test framework object posted")
    public void single_test_framework_object_posted() throws IOException{
        Framework f = new Framework();
        f.setName(frameworkPrefix + "0");
        HttpEntity<Framework> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(f), header);
        ResponseEntity<Framework> frameworkResponse = restTemplate.exchange(frameworksUrl, HttpMethod.POST, request, singleFrameworkTypeRef);
        savedFramework = frameworkResponse.getBody();
    }

    @When("^the client makes a GET framework request$")
    public void the_client_issues_GET_BY_ID_REQUEST(){
        singleFrameworkResponse = restTemplate.exchange(frameworksUrl + savedFramework.getId(), HttpMethod.GET, null, singleFrameworkTypeRef);
    }

    @And("^the client receives the correct framework")
    public void the_client_receives_the_correct_framework(){
        Framework f = singleFrameworkResponse.getBody();
        assertEquals(savedFramework.getId(), f.getId());
        assertEquals(savedFramework.getName(), f.getName());
        cleanFramework(singleFrameworkResponse);
    }

    @When("^the client makes a POST framework request")
    public void the_client_makes_a_POST_framework_request() throws IOException{
        Framework f = new Framework();
        f.setName(frameworkPrefix + "0");
        savedFramework = f;
        HttpEntity<Framework> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(f), header);
        singleFrameworkResponse = restTemplate.exchange(frameworksUrl, HttpMethod.POST, request, singleFrameworkTypeRef);
    }

    @And("^the framework is added to the database")
    public void the_framework_is_added_to_the_database(){
        ResponseEntity<Framework> frameworkResponse = restTemplate.exchange(frameworksUrl + singleFrameworkResponse.getBody().getId(), HttpMethod.GET, null, singleFrameworkTypeRef);
        assertFalse(frameworkResponse == null);
    }

    @And("^the client receives the correct added framework")
    public void the_client_receives_the_correct_added_framework(){
        Framework f = singleFrameworkResponse.getBody();
        assertEquals(savedFramework.getName(), f.getName());
        cleanFramework(singleFrameworkResponse);
    }

    @When("^the client makes a PUT framework request with a new framework")
    public void the_client_makes_a_PUT_framework_request_with_a_new_framework() throws IOException{
        Framework f = new Framework();
        f.setName(frameworkPrefix + "1");
        savedFramework.setName(f.getName());
        HttpEntity<Framework> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(f), header);
        singleFrameworkResponse = restTemplate.exchange(frameworksUrl + savedFramework.getId(), HttpMethod.PUT, request, singleFrameworkTypeRef);
        savedFramework = singleFrameworkResponse.getBody();
    }

    @And("^the framework is updated in the database")
    public void the_framework_is_updated_in_the_database(){
        ResponseEntity<Framework> frameworkResponse = restTemplate.exchange(frameworksUrl + savedFramework.getId(), HttpMethod.GET, null, singleFrameworkTypeRef);
        assertEquals(savedFramework.getName(), frameworkResponse.getBody().getName());
    }
}
