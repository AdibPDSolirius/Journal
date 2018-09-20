package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private FrameworkRepository frameworkRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private DatabaseRepository databaseRepository;



    public List<Resource> findAll(){
        List<Resource> resources =  resourceRepository.findAll();

        return resources;
    }

    public Resource save(Resource resource){
        return resourceRepository.save(resource);
    }

    public Set<Resource> filterResourcesByLanguageId(Long languageId){
        return languageRepository.findById(languageId).get().getResources();
    }

    public Set<Resource> filterResourcesByFrameworkId(Long frameworkId){
        return frameworkRepository.findById(frameworkId).get().getResources();
    }

    public Set<Resource> filterResourcesByLibraryId(Long libraryId){
        return libraryRepository.findById(libraryId).get().getResources();
    }

    public Set<Resource> filterResourcesByDatabaseId(Long databaseId){
        return databaseRepository.findById(databaseId).get().getResources();
    }

    public void deleteById(Long id){ resourceRepository.deleteById(id); };

}
