package com.journal.adib.Journal;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Long> {
    public List<Resource> findAll();
}
