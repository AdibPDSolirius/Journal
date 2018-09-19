package com.journal.adib.Journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;


    public List<Resource> findAll(){
        List<Resource> resources =  resourceRepository.findAll();

        return resources;
    }

    public Resource saveResource(Resource resource){
        return resourceRepository.save(resource);
    }

}
