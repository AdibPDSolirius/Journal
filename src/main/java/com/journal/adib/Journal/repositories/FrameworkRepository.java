package com.journal.adib.Journal.repositories;

import com.journal.adib.Journal.models.Framework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FrameworkRepository extends CrudRepository<Framework, Long> {
    List<Framework> findAll();
}
