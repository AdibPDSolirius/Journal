package com.journal.adib.Journal.Repositories;

import com.journal.adib.Journal.Models.Framework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FrameworkRepository extends CrudRepository<Framework, Long> {
    List<Framework> findAll();
}
