package com.journal.adib.Journal.services;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Framework;
import com.journal.adib.Journal.repositories.FrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FrameworkService {

    @Autowired
    private FrameworkRepository frameworkRepository;

    public List<Framework> findAll() throws JournalException {
        List<Framework> frameworkList = frameworkRepository.findAll();
        if(frameworkList.size() != 0){
            return frameworkList;
        }else{
            throw new JournalException("No frameworks found", HttpStatus.NO_CONTENT);
        }
    }

    public Framework findById(Long id) throws JournalException {
        Optional<Framework> of = frameworkRepository.findById(id);
        if(of.isPresent()){
            return of.get();
        }else{
            throw new JournalException("Framework not found", HttpStatus.NOT_FOUND);
        }
    }

    public Framework save(Framework framework) throws JournalException{
        Framework savedFramework = frameworkRepository.save(framework);
        if(savedFramework != null){
            return savedFramework;
        }else{
            throw new JournalException("Failed to save framework", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteById(Long id){ frameworkRepository.deleteById(id); };

}
