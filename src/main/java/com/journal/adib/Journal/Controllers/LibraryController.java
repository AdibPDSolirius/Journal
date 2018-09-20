package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Database;
import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Models.Library;
import com.journal.adib.Journal.Services.DatabaseService;
import com.journal.adib.Journal.Services.FrameworkService;
import com.journal.adib.Journal.Services.LanguageService;
import com.journal.adib.Journal.Services.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
public class LibraryController {

    @Autowired
    LibraryService libraryService;


    @GetMapping("/libraries")
    public String find(Model model) {
        List<Library> libraries = (List<Library>) libraryService.findAll();
        model.addAttribute("libraries",libraries);
        return "library-list";

    }

    @PostMapping("/libraries")
    @ResponseBody
    public Library create(@RequestBody Library libraries){
        return libraryService.save(libraries);
    }

    @DeleteMapping("/libraries/{libraryId}")
    @ResponseBody
    public void deleteById(@PathVariable Long id){
        libraryService.deleteById(id);
    }


}
