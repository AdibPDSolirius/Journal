package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.Language;
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

public class LanguageIntegrationSteps {
    Language savedLanguage;
    HttpHeaders header;
    RestTemplate restTemplate;
    ParameterizedTypeReference<Language> singleLanguageTypeRef;
    ParameterizedTypeReference<List<Language>> multipleLanguageTypeRef;
    ResponseEntity<List<Language>> multipleLanguageResponse;
    ResponseEntity<Language> singleLanguageResponse;
    List<Language> savedLanguages;
    String languagePrefix;
    String languagesUrl;

    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        singleLanguageTypeRef = new ParameterizedTypeReference<Language>() {};
        multipleLanguageTypeRef = new ParameterizedTypeReference<List<Language>>() {};
        savedLanguages = new ArrayList<>();
        languagePrefix = "testLanguage";
        languagesUrl = "http://localhost:8080/api/languages/";

        cleanLanguages(restTemplate.exchange(languagesUrl, HttpMethod.GET, null, multipleLanguageTypeRef));
    }

    void cleanLanguages(ResponseEntity<List<Language>> res){
        if(res == null){
            return;
        }
        if(res.getBody() == null){
            return;
        }
        for(Language l: res.getBody()){
            if(l.getName().contains(languagePrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(languagesUrl + l.getId(), HttpMethod.DELETE, null,
                        new ParameterizedTypeReference<Language>() {
                        });
            }
        }
    }

    void cleanLanguage(ResponseEntity<Language> res){
        if(res == null){
            return;
        }
        Language l = res.getBody();
        if(l.getName().contains(languagePrefix)){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(languagesUrl + l.getId(), HttpMethod.DELETE, null, singleLanguageTypeRef);
        }
    }

    @Given("^test language objects posted")
    public void test_language_objects_posted() throws IOException {
        for(int i = 0; i < 3; i++){
            Language l = new Language();
            l.setName(languagePrefix + i);

            HttpEntity<Language> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
            ResponseEntity<Language> response = restTemplate.exchange(languagesUrl, HttpMethod.POST, request, singleLanguageTypeRef);
            savedLanguages.add(response.getBody());
        }
    }


    @When("^the client calls /api/languages")
    public void the_client_issues_GET_ALL_LANGUAGES_REQUEST(){
        multipleLanguageResponse = restTemplate.exchange(languagesUrl, HttpMethod.GET, null, multipleLanguageTypeRef);
    }

    @Then("^the client receives language status code of (\\d+)$")
    public void the_client_receives_language_status_code_of(int statusCode){
        if(singleLanguageResponse != null){
            assertEquals(statusCode, singleLanguageResponse.getStatusCode().value());
        }else if(multipleLanguageResponse != null){
            assertEquals(statusCode, multipleLanguageResponse.getStatusCode().value());
        }
    }

    @And("^the client receives the languages")
    public void the_client_receives_the_languages(){
        int size = 0;
        for(Language l: multipleLanguageResponse.getBody()){
            if(l.getName().contains(languagePrefix)){
                size++;
            }
        }
        assertEquals(savedLanguages.size(), size);
        cleanLanguages(multipleLanguageResponse);
    }

    @Given("^single test language object posted")
    public void single_test_language_object_posted() throws IOException{
        Language l = new Language();
        l.setName(languagePrefix + "0");
        HttpEntity<Language> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        ResponseEntity<Language> languageResponse= restTemplate.exchange(languagesUrl, HttpMethod.POST, request, singleLanguageTypeRef);
        savedLanguage = languageResponse.getBody();
    }

    @When("^the client makes a GET language request$")
    public void the_client_issues_GET_BY_ID_REQUEST(){
        singleLanguageResponse = restTemplate.exchange(languagesUrl + savedLanguage.getId(), HttpMethod.GET, null, singleLanguageTypeRef);
    }

    @And("^the client receives the correct language")
    public void the_client_receives_the_correct_language(){
        Language l = singleLanguageResponse.getBody();
        assertEquals(savedLanguage.getId(), l.getId());
        assertEquals(savedLanguage.getName(), l.getName());
        cleanLanguage(singleLanguageResponse);
    }

    @When("^the client makes a POST language request")
    public void the_client_makes_a_POST_language_request() throws IOException{
        Language l = new Language();
        l.setName(languagePrefix + "0");
        savedLanguage = l;
        HttpEntity<Language> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        singleLanguageResponse = restTemplate.exchange(languagesUrl, HttpMethod.POST, request, singleLanguageTypeRef);
    }

    @And("^the language is added to the database")
    public void the_language_is_added_to_the_database(){
        ResponseEntity<Language> languageResponse = restTemplate.exchange(languagesUrl + singleLanguageResponse.getBody().getId(), HttpMethod.GET, null, singleLanguageTypeRef);
        assertFalse(languageResponse == null);
    }

    @And("^the client receives the correct added language")
    public void the_client_receives_the_correct_added_language(){
        Language l = singleLanguageResponse.getBody();
        assertEquals(savedLanguage.getName(), l.getName());
        cleanLanguage(singleLanguageResponse);
    }

    @When("^the client makes a PUT language request with a new language")
    public void the_client_makes_a_PUT_language_request_with_a_new_language() throws IOException{
        Language l = new Language();
        l.setName(languagePrefix + "1");
        savedLanguage.setName(l.getName());
        HttpEntity<Language> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header);
        singleLanguageResponse = restTemplate.exchange(languagesUrl + savedLanguage.getId(), HttpMethod.PUT, request, singleLanguageTypeRef);
        savedLanguage = singleLanguageResponse.getBody();
    }

    @And("^the language is updated in the database")
    public void the_language_is_updated_in_the_database(){
        ResponseEntity<Language> languageResponse = restTemplate.exchange(languagesUrl + savedLanguage.getId(), HttpMethod.GET, null, singleLanguageTypeRef);
        assertEquals(savedLanguage.getName(), languageResponse.getBody().getName());
    }
}
