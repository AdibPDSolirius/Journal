package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
public class LanguageController {

    @Autowired
    LanguageService languageService;


    @GetMapping("/languages")
    public String findResources(Model model) {
        List<Language> languages = (List<Language>) languageService.findAll();
        model.addAttribute("languages",languages);
        return "language-list";

    }

    @PostMapping("/languages")
    @ResponseBody
    public Language createLanguage(@RequestBody Language language){
        return languageService.saveLanguage(language);
    }


}
