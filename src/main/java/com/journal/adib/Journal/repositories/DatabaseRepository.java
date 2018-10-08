package com.journal.adib.Journal.repositories;
import com.journal.adib.Journal.models.Database;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DatabaseRepository extends CrudRepository<Database, Long> {
    List<Database> findAll();
}