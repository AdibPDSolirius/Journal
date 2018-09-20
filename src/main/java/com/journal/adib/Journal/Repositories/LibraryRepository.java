package com.journal.adib.Journal.Repositories;

import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Models.Library;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibraryRepository extends CrudRepository<Library, Long> {
    List<Library> findAll();
}
