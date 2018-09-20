package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;


@Controller
public class ResourceController {

    @Autowired
    ResourceService resourceService;


    @GetMapping("/resources")
    public String find(Model model) {
        List<Resource> resources = resourceService.findAll();
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @GetMapping("/resources/language/{languageId}")
    public String findByLanguageId(@PathVariable(value="languageId") Long languageId, Model model) {
        Set<Resource> resources = resourceService.filterResourcesByLanguageId(languageId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @GetMapping("/resources/framework/{frameworkId}")
    public String findByFrameworkId(@PathVariable(value="frameworkId") Long frameworkId, Model model) {
        Set<Resource> resources = resourceService.filterResourcesByFrameworkId(frameworkId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @GetMapping("/resources/database/{databaseId}")
    public String findByDatabaseId(@PathVariable(value="databaseId") Long databaseId, Model model) {
        Set<Resource> resources = resourceService.filterResourcesByLanguageId(databaseId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @GetMapping("/resources/library/{libraryId}")
    public String findByLibraryId(@PathVariable(value="libraryId") Long libraryId, Model model) {
        Set<Resource> resources = resourceService.filterResourcesByLanguageId(libraryId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @PostMapping("/resources")
    @ResponseBody
    public Resource create(@RequestBody Resource resource){
        return resourceService.save(resource);
    }

    @DeleteMapping("/resource/{resourceId}")
    @ResponseBody
    public void deleteById(@PathVariable Long id){
        resourceService.deleteById(id);
    }


}
