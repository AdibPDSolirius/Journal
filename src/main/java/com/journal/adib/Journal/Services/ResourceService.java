package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;


    public List<Resource> findAll(){
        List<Resource> resources =  resourceRepository.findAll();

        return resources;
    }

    public Resource save(Resource resource){
        return resourceRepository.save(resource);
    }

    public void deleteById(Long id){ resourceRepository.deleteById(id); };

}
