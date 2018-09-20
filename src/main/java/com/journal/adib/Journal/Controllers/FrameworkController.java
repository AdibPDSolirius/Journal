package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Framework;
import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Services.FrameworkService;
import com.journal.adib.Journal.Services.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
public class FrameworkController {

    @Autowired
    FrameworkService frameworkService;


    @GetMapping("/frameworks")
    public String find(Model model) {
        List<Framework> frameworks = (List<Framework>) frameworkService.findAll();
        model.addAttribute("frameworks",frameworks);
        return "framework-list";

    }

    @PostMapping("/frameworks")
    @ResponseBody
    public Framework create(@RequestBody Framework framework){ return frameworkService.save(framework); }

    @DeleteMapping("/frameworks/{frameworkId}")
    @ResponseBody
    public void deleteById(@PathVariable(value="frameworkId") Long frameworkId){ frameworkService.deleteById(frameworkId); }


}
