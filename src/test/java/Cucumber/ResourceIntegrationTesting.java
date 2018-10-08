package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.Resource;
import cucumber.api.java.After;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class ResourceIntegrationTesting {

    ParameterizedTypeReference<Resource> singleResourceTypeRef;
    ParameterizedTypeReference<List<Resource>> multipleResourceTypeRef;
    ResponseEntity<List<Resource>> response;
    Set<Resource> savedResources;
    String resourcePrefix;
    HttpHeaders header;

    @Before
    public void setup(){
        singleResourceTypeRef = new ParameterizedTypeReference<Resource>() {};
        multipleResourceTypeRef = new ParameterizedTypeReference<List<Resource>>() {};
        savedResources = new HashSet<>();
        resourcePrefix = "testResource";
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
    }

    @After
    public void cleanup(){
        for(Resource r: response.getBody()){
            if(r.getName().contains(resourcePrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange("http://localhost:8080/api/resources/" + r.getId(), HttpMethod.DELETE, null, singleResourceTypeRef);
            }
        }
    }

    @Given("^test resource objects posted")
    public void testResourcePosting() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String resourcesUrl = "http://localhost:8080/api/resources";
        for(int i = 0; i < 3; i++){
            Resource r = new Resource();
            r.setName(resourcePrefix + i);
            r.setUrl(resourcePrefix + i + "url" );
            HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
            savedResources.add(response.getBody());
        }
    }

    @When("^the client calls /api/resources$")
    public void the_client_issues_GET_version(){
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Resource>> typeRef = new ParameterizedTypeReference<List<Resource>>() {};
        String resourcesUrl = "http://localhost:8080/api/resources";
        response = restTemplate.exchange(resourcesUrl, HttpMethod.GET, null, typeRef);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode){
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("^the client receives the resources")
    public void the_client_receives_the_resources(){
        int size = 0;
        for(Resource r: response.getBody()){
            if(r.getName().contains(resourcePrefix)){
                size++;
            }
        }
        assertEquals(savedResources.size(), size);
    }

}
