package com.journal.adib.Journal.Repositories;

import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Language;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FrameworkRepository extends CrudRepository<Framework, Long> {
    List<Framework> findAll();
}
