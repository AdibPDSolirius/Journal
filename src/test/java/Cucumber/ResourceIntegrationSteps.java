package Cucumber;

import com.journal.adib.Journal.TestUtil;
import com.journal.adib.Journal.models.*;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.lexer.Fr;
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
import static org.junit.Assert.assertFalse;


public class ResourceIntegrationSteps {

    Resource savedResource;
    HttpHeaders header;
    RestTemplate restTemplate;
    ParameterizedTypeReference<Resource> singleResourceTypeRef;
    ParameterizedTypeReference<List<Resource>> multipleResourceTypeRef;
    ResponseEntity<List<Resource>> multipleResourceResponse;
    ResponseEntity<Resource> singleResourceResponse;
    List<Resource> savedResources;
    List<Database> savedDatabases;
    List<Framework> savedFrameworks;
    List<Language> savedLanguages;
    List<Library> savedLibraries;
    String resourcePrefix;
    String databasePrefix;
    String frameworkPrefix;
    String languagePrefix;
    String libraryPrefix;
    String resourcesUrl;
    String databasesUrl;
    String frameworksUrl;
    String languagesUrl;
    String librariesUrl;



    @Before
    public void setup(){
        restTemplate = new RestTemplate();
        header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        singleResourceTypeRef = new ParameterizedTypeReference<Resource>() {};
        multipleResourceTypeRef = new ParameterizedTypeReference<List<Resource>>() {};
        savedResources = new ArrayList<>();
        savedDatabases = new ArrayList<>();
        savedFrameworks = new ArrayList<>();
        savedLanguages = new ArrayList<>();
        savedLibraries = new ArrayList<>();
        resourcePrefix = "testResource";
        databasePrefix = "testDatabase";
        frameworkPrefix = "testFramework";
        languagePrefix = "testLanguage";
        libraryPrefix = "testLibrary";
        resourcesUrl = "http://localhost:8080/api/resources/";
        databasesUrl = "http://localhost:8080/api/databases/";
        frameworksUrl = "http://localhost:8080/api/frameworks/";
        languagesUrl = "http://localhost:8080/api/languages/";
        librariesUrl = "http://localhost:8080/api/libraries/";

        cleanResources(restTemplate.exchange(resourcesUrl, HttpMethod.GET, null, multipleResourceTypeRef));
        cleanDatabases(restTemplate.exchange(databasesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Database>>() {
                }));
        cleanFrameworks(restTemplate.exchange(frameworksUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Framework>>() {
                }));
        cleanLanguages(restTemplate.exchange(languagesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Language>>() {
                }));
        cleanLibraries(restTemplate.exchange(librariesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Library>>() {
                }));
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

    void cleanResources(ResponseEntity<List<Resource>> res){
        if(res == null){
            return;
        }
        if(res.getBody() == null){
            return;
        }
        for(Resource r: res.getBody()){
            if(r.getName().contains(resourcePrefix)){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(resourcesUrl + r.getId(), HttpMethod.DELETE, null, singleResourceTypeRef);
            }
        }
    }

    @Given("^test resource objects posted")
    public void test_resource_objects_posted() throws IOException {
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
    public void the_client_issues_GET_ALL_RESOURCES_REQUEST(){
        multipleResourceResponse = restTemplate.exchange(resourcesUrl, HttpMethod.GET, null, multipleResourceTypeRef);
    }

    @Then("^the client receives resource status code of (\\d+)$")
    public void the_client_receives_resource_status_code_of(int statusCode){
        if(singleResourceResponse != null){
            assertEquals(statusCode, singleResourceResponse.getStatusCode().value());
        }else if(multipleResourceResponse != null){
            assertEquals(statusCode, multipleResourceResponse.getStatusCode().value());
        }
    }

    @And("^the client receives the resources")
    public void the_client_receives_the_resources(){
        int size = 0;
        for(Resource r: multipleResourceResponse.getBody()){
            if(r.getName().contains(resourcePrefix)){
                size++;
            }
        }
        assertEquals(savedResources.size(), size);
        cleanResources(multipleResourceResponse);
    }

    @Given("^single test resource object posted")
    public void single_test_resource_object_posted() throws IOException{
        Resource r = new Resource();
        r.setName(resourcePrefix + "0");
        r.setUrl(resourcePrefix + "0" + "url" );
        HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
        ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
        savedResource = response.getBody();
    }

    @When("^the client makes a GET resource request$")
    public void the_client_issues_GET_BY_ID_REQUEST(){
        singleResourceResponse = restTemplate.exchange(resourcesUrl + savedResource.getId(), HttpMethod.GET, null, singleResourceTypeRef);
    }

    @And("^the client receives the correct resource")
    public void the_client_receives_the_correct_resource(){
        Resource r = singleResourceResponse.getBody();
        assertEquals(savedResource.getId(), r.getId());
        assertEquals(savedResource.getName(), r.getName());
        assertEquals(savedResource.getUrl(), r.getUrl());
        cleanResource(singleResourceResponse);
    }

    void cleanResource(ResponseEntity<Resource> res){
        if(res == null){
            return;
        }
        Resource r = res.getBody();
        if(r.getName().contains(resourcePrefix)){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(resourcesUrl + r.getId(), HttpMethod.DELETE, null, singleResourceTypeRef);
        }
    }

    @When("^the client makes a POST resource request")
    public void the_client_makes_a_POST_resource_request() throws IOException{
        Resource r = new Resource();
        r.setName(resourcePrefix + "0");
        r.setUrl(resourcePrefix + "0" + "url" );
        savedResource = r;
        HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
        singleResourceResponse = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
    }

    @And("^the resource is added to the database")
    public void the_resource_is_added_to_the_database(){
        ResponseEntity<Resource> resourceResponse = restTemplate.exchange(resourcesUrl + singleResourceResponse.getBody().getId(), HttpMethod.GET, null, singleResourceTypeRef);
        assertFalse(resourceResponse == null);
    }

    @And("^the client receives the correct added resource")
    public void the_client_receives_the_correct_added_resource(){
        Resource r = singleResourceResponse.getBody();
        assertEquals(savedResource.getName(), r.getName());
        assertEquals(savedResource.getUrl(), r.getUrl());
        cleanResource(singleResourceResponse);
    }

    @When("^the client makes a PUT resource request with a new resource")
    public void the_client_makes_a_PUT_resource_request_with_a_new_resource() throws IOException{
        Resource r = new Resource();
        r.setName(resourcePrefix + "1");
        r.setUrl(resourcePrefix + "1" + "url" );
        savedResource.setName(r.getName());
        savedResource.setUrl(r.getUrl());
        HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
        singleResourceResponse = restTemplate.exchange(resourcesUrl + savedResource.getId(), HttpMethod.PUT, request, singleResourceTypeRef);
        savedResource = singleResourceResponse.getBody();
    }

    @And("^the resource is updated in the database")
    public void the_resource_is_updated_in_the_database(){
        ResponseEntity<Resource> resourceResponse = restTemplate.exchange(resourcesUrl + savedResource.getId(), HttpMethod.GET, null, singleResourceTypeRef);
        assertEquals(savedResource.getName(), resourceResponse.getBody().getName());
        assertEquals(savedResource.getUrl(), resourceResponse.getBody().getUrl());
    }

    @Given("^test resource objects with databases posted")
    public void test_resource_objects_with_databases_posted() throws IOException{
        for(int i = 0; i < 3; i++) {
            Resource r = new Resource();
            r.setName(resourcePrefix + i);
            r.setUrl(resourcePrefix + i + "url");

            Database d = new Database();
            d.setName(databasePrefix + i);
            ResponseEntity<Database> databaseResponse = restTemplate.exchange(databasesUrl,
                    HttpMethod.POST,
                    new HttpEntity(TestUtil.convertObjectToJsonBytes(d), header),
                    new ParameterizedTypeReference<Database>() {
                    });
            Set<Database> databaseSet = new HashSet<>();
            databaseSet.add(databaseResponse.getBody());
            savedDatabases.add(databaseResponse.getBody());
            r.setDatabases(databaseSet);

            HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
            savedResources.add(response.getBody());
        }
    }

    @When("^the client makes a GET resource request with a database ID")
    public void the_client_makes_a_GET_resource_request_with_a_database_ID(){
        multipleResourceResponse = restTemplate.exchange(resourcesUrl + "/database/" + savedDatabases.get(0).getId(), HttpMethod.GET, null, multipleResourceTypeRef);
    }

    @And("^the client receives the correctly database filtered resources")
    public void the_client_receives_the_correctly_database_filtered_resources(){
        int size = 0;
        for(Resource r: multipleResourceResponse.getBody()){
            if(r.getName().contains(resourcePrefix)){
                for(Database d: r.getDatabases()){
                    if(d.getId().toString().equals(savedDatabases.get(0).getId().toString())){
                        size ++;
                        break;
                    }
                }
            }
        }
        assertEquals(1, size);
        cleanDatabases(restTemplate.exchange(databasesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Database>>() {
                }));
        cleanResources(restTemplate.exchange(resourcesUrl,
                HttpMethod.GET,
                null,
                multipleResourceTypeRef));
    }


    @Given("^test resource objects with frameworks posted")
    public void test_resource_objects_with_frameworks_posted() throws IOException{
        for(int i = 0; i < 3; i++) {
            Resource r = new Resource();
            r.setName(resourcePrefix + i);
            r.setUrl(resourcePrefix + i + "url");

            Framework f = new Framework();
            f.setName(frameworkPrefix + i);
            ResponseEntity<Framework> frameworkResponse = restTemplate.exchange(frameworksUrl,
                    HttpMethod.POST,
                    new HttpEntity(TestUtil.convertObjectToJsonBytes(f), header),
                    new ParameterizedTypeReference<Framework>() {
                    });
            Set<Framework> frameworkSet = new HashSet<>();
            frameworkSet.add(frameworkResponse.getBody());
            savedFrameworks.add(frameworkResponse.getBody());
            r.setFrameworks(frameworkSet);

            HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
            savedResources.add(response.getBody());
        }
    }

    @When("^the client makes a GET resource request with a framework ID")
    public void the_client_makes_a_GET_resource_request_with_a_framework_ID(){
        multipleResourceResponse = restTemplate.exchange(resourcesUrl + "/framework/" + savedFrameworks.get(0).getId(), HttpMethod.GET, null, multipleResourceTypeRef);
    }

    @And("^the client receives the correctly framework filtered resources")
    public void the_client_receives_the_correctly_framework_filtered_resources(){
        int size = 0;
        for(Resource r: multipleResourceResponse.getBody()){
            if(r.getName().contains(resourcePrefix)){
                for(Framework f: r.getFrameworks()){
                    if(f.getId().toString().equals(savedFrameworks.get(0).getId().toString())){
                        size ++;
                        break;
                    }
                }
            }
        }
        assertEquals(1, size);
        cleanFrameworks(restTemplate.exchange(frameworksUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Framework>>() {
                }));
        cleanResources(restTemplate.exchange(resourcesUrl,
                HttpMethod.GET,
                null,
                multipleResourceTypeRef));
    }

    @Given("^test resource objects with languages posted")
    public void test_resource_objects_with_languages_posted() throws IOException{
        for(int i = 0; i < 3; i++) {
            Resource r = new Resource();
            r.setName(resourcePrefix + i);
            r.setUrl(resourcePrefix + i + "url");

            Language l = new Language();
            l.setName(languagePrefix + i);
            ResponseEntity<Language> languageResponse = restTemplate.exchange(languagesUrl,
                    HttpMethod.POST,
                    new HttpEntity(TestUtil.convertObjectToJsonBytes(l), header),
                    new ParameterizedTypeReference<Language>() {});
            Set<Language> languageSet = new HashSet<>();
            languageSet.add(languageResponse.getBody());
            savedLanguages.add(languageResponse.getBody());
            r.setLanguages(languageSet);

            HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
            savedResources.add(response.getBody());
        }
    }

    @When("^the client makes a GET resource request with a language ID")
    public void the_client_makes_a_GET_resource_request_with_a_language_ID(){
        multipleResourceResponse = restTemplate.exchange(resourcesUrl + "/language/" + savedLanguages.get(0).getId(), HttpMethod.GET, null, multipleResourceTypeRef);
    }

    @And("^the client receives the correctly language filtered resources")
    public void the_client_receives_the_correctly_language_filtered_resources(){
        int size = 0;
        for(Resource r: multipleResourceResponse.getBody()){
            if(r.getName().contains(resourcePrefix)){
                for(Language l: r.getLanguages()){
                    if(l.getId().toString().equals(savedLanguages.get(0).getId().toString())){
                        size ++;
                        break;
                    }
                }
            }
        }
        assertEquals(1, size);
        cleanLanguages(restTemplate.exchange(languagesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Language>>() {
                }));
        cleanResources(restTemplate.exchange(resourcesUrl,
                HttpMethod.GET,
                null,
                multipleResourceTypeRef));
    }

    @Given("^test resource objects with libraries posted")
    public void test_resource_objects_with_libraries_posted() throws IOException{
        for(int i = 0; i < 3; i++){
            Resource r = new Resource();
            r.setName(resourcePrefix + i);
            r.setUrl(resourcePrefix + i + "url" );

            Library li = new Library();
            li.setName(libraryPrefix + i);
            ResponseEntity<Library> libraryResponse = restTemplate.exchange(librariesUrl,
                    HttpMethod.POST,
                    new HttpEntity(TestUtil.convertObjectToJsonBytes(li), header),
                    new ParameterizedTypeReference<Library>() {});
            Set<Library> librarySet= new HashSet<>();
            librarySet.add(libraryResponse.getBody());
            savedLibraries.add(libraryResponse.getBody());
            r.setLibraries(librarySet);

            HttpEntity<Resource> request = new HttpEntity(TestUtil.convertObjectToJsonBytes(r), header);
            ResponseEntity<Resource> response = restTemplate.exchange(resourcesUrl, HttpMethod.POST, request, singleResourceTypeRef);
            savedResources.add(response.getBody());
        }
    }

    @When("^the client makes a GET resource request with a library ID")
    public void the_client_makes_a_GET_resource_request_with_a_library_ID(){
        multipleResourceResponse = restTemplate.exchange(resourcesUrl + "/library/" + savedLibraries.get(0).getId(), HttpMethod.GET, null, multipleResourceTypeRef);
    }

    @And("^the client receives the correctly library filtered resources")
    public void the_client_receives_the_correctly_library_filtered_resources(){
        int size = 0;
        for(Resource r: multipleResourceResponse.getBody()){
            if(r.getName().contains(resourcePrefix)){
                for(Library l: r.getLibraries()){
                    if(l.getId().toString().equals(savedLibraries.get(0).getId().toString())){
                        size ++;
                        break;
                    }
                }
            }
        }
        assertEquals(1, size);
        cleanLibraries(restTemplate.exchange(librariesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Library>>() {
                }));
        cleanResources(restTemplate.exchange(resourcesUrl,
                HttpMethod.GET,
                null,
                multipleResourceTypeRef));
    }

}
