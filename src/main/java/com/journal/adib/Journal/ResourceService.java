package com.journal.adib.Journal;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> getAllResourceObjects(){
        Iterable<Resource> resourcesIt = resourceRepository.findAll();
        List<Resource> resources = new ArrayList<>();
        Iterator i = resourcesIt.iterator();
        while(i.hasNext()){
            resources.add((Resource) i.next());

        }
        return resources;
    }

}
