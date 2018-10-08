package com.journal.adib.Journal.Repositories;

import com.journal.adib.Journal.models.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language, Long> {
    List<Language> findAll();
}
