package com.journal.adib.Journal.services;

import com.journal.adib.Journal.errorHandling.JournalException;
import com.journal.adib.Journal.models.Database;
import com.journal.adib.Journal.repositories.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    public List<Database> findAll() throws JournalException {
        List<Database> databaseList = databaseRepository.findAll();
        if(databaseList.size() != 0){
            return databaseList;
        }else{
            throw new JournalException("No databases found", HttpStatus.NO_CONTENT);
        }
    }

    public Database findById(Long id) throws JournalException {
        Optional<Database> of = databaseRepository.findById(id);
        if(of.isPresent()){
            return of.get();
        }else{
            throw new JournalException("Database not found", HttpStatus.NOT_FOUND);
        }
    }

    public Database save(Database database) throws JournalException{
        Database savedDatabase = databaseRepository.save(database);
        if(savedDatabase != null){
            return savedDatabase;
        }else{
            throw new JournalException("Failed to save database", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteById(Long id){ databaseRepository.deleteById(id); };

}
