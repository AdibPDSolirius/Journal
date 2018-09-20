package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Repositories.FrameworkRepository;
import com.journal.adib.Journal.Repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkService {

    @Autowired
    private FrameworkRepository frameworkRepository;


    public List<Framework> findAll(){
        List<Framework> frameworks =  frameworkRepository.findAll();

        return frameworks;
    }

    public Framework save(Framework framework){
        return frameworkRepository.save(framework);
    }

    public void deleteById(Long id){ frameworkRepository.deleteById(id); };

}
