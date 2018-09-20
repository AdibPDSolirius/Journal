package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.Models.Database;
import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Repositories.DatabaseRepository;
import com.journal.adib.Journal.Repositories.FrameworkRepository;
import com.journal.adib.Journal.Repositories.LibraryRepository;
import com.journal.adib.Journal.Repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;


    public List<Library> findAll(){
        List<Library> libraries =  libraryRepository.findAll();

        return libraries;
    }

    public Library save(Library library){
        return libraryRepository.save(library);
    }

    public void deleteById(Long id){ libraryRepository.deleteById(id); };

}
