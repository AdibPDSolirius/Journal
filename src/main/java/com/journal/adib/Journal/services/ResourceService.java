package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Resource;
import com.journal.adib.Journal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private FrameworkService frameworkService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private DatabaseService databaseService;



    public List<Resource> findAll() throws JournalException{
        List<Resource> resourceList = resourceRepository.findAll();
        if(resourceList.size() != 0){
            return resourceList;
        }else{
            throw new JournalException("No resources found", HttpStatus.NO_CONTENT);
        }
    }

    public Resource findById(Long id) throws JournalException {
        Optional<Resource> or = resourceRepository.findById(id);
        if(or.isPresent()){
            return or.get();
        }else{
            throw new JournalException("Resource not found", HttpStatus.NOT_FOUND);
        }
    }

    public Resource save(Resource resource) throws JournalException{
        Resource savedResource = resourceRepository.save(resource);
        if(savedResource != null){
            return savedResource;
        }else{
            throw new JournalException("Failed to save resource", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Resource> filterResourcesByLanguageId(Long languageId) throws JournalException{
        return processResourcesFound(languageService.findById(languageId).getResources());
    }

    public List<Resource> filterResourcesByFrameworkId(Long frameworkId) throws JournalException{
        return processResourcesFound(frameworkService.findById(frameworkId).getResources());
    }

    public List<Resource> filterResourcesByLibraryId(Long libraryId) throws JournalException{
        return processResourcesFound(libraryService.findById(libraryId).getResources());
    }

    public List<Resource> filterResourcesByDatabaseId(Long databaseId) throws JournalException{
        return processResourcesFound(databaseService.findById(databaseId).getResources());
    }

    public void deleteById(Long id){
        resourceRepository.deleteById(id);
    }

    private List<Resource> processResourcesFound(Set<Resource> resources) throws JournalException{
        if(resources.size() != 0){
            List<Resource> resourceList = new ArrayList<>();
            resourceList.addAll(resources);
            return resourceList;
        }else{
            throw new JournalException("No resources found", HttpStatus.NO_CONTENT);
        }
    }



}
