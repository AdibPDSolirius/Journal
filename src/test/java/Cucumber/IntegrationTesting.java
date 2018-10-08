package Cucumber;

import com.journal.adib.Journal.models.Resource;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import static org.junit.Assert.assertEquals;


public class IntegrationTesting {

    ResponseEntity<List<Resource>> response;

    @When("^the client calls /resources$")
    public void the_client_issues_GET_version(){
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Resource>> typeRef = new ParameterizedTypeReference<List<Resource>>() {};
        String resourcesUrl = "http://localhost:8080/resources";
        response = restTemplate.exchange(resourcesUrl, HttpMethod.GET, null, typeRef);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode){
        assertEquals(statusCode, response.getStatusCode().value());
    }

    @And("^the client receives the resources")
    public void the_client_receives_the_resources(){
        assertEquals(7, response.getBody().size());
    }
}
