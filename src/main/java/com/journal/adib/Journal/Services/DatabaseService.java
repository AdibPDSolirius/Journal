package com.journal.adib.Journal.Services;
import com.journal.adib.Journal.Models.Database;
import com.journal.adib.Journal.Repositories.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    public List<Database> findAll(){
        List<Database> databases =  databaseRepository.findAll();
        return databases;
    }

    public Database save(Database database){
        return databaseRepository.save(database);
    }
    public void deleteById(Long id){ databaseRepository.deleteById(id); };
}