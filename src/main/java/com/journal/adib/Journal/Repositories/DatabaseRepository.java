package com.journal.adib.Journal.Repositories;
import com.journal.adib.Journal.Models.Database;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DatabaseRepository extends CrudRepository<Database, Long> {
    List<Database> findAll();
}