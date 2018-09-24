package com.journal.adib.Journal.Services;

import com.journal.adib.Journal.ErrorHandling.JournalException;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Repositories.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    public List<Library> findAll() throws JournalException {
        List<Library> libraryList = libraryRepository.findAll();
        if(libraryList.size() != 0){
            return libraryList;
        }else{
            throw new JournalException("No libraries found", HttpStatus.NO_CONTENT);
        }
    }

    public Library findById(Long id) throws JournalException {
        Optional<Library> ol = libraryRepository.findById(id);
        if(ol.isPresent()){
            return ol.get();
        }else{
            throw new JournalException("Library not found", HttpStatus.NOT_FOUND);
        }
    }

    public Library save(Library library) throws JournalException{
        Library savedLibrary = libraryRepository.save(library);
        if(savedLibrary != null){
            return savedLibrary;
        }else{
            throw new JournalException("Failed to save savedLibrary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteById(Long id) { libraryRepository.deleteById(id); }

}
