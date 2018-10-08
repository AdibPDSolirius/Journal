package com.journal.adib.Journal.Repositories;

import com.journal.adib.Journal.models.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Long> {
    List<Resource> findAll();
}
