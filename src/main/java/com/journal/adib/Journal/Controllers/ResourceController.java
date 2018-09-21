package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/resources/{resourceId}")
    public String find(@PathVariable(value="resourceId") Long resoureceId, Model model) {
        Resource resource = resourceService.findById(resoureceId);
        List<Resource> resources = new ArrayList<>();
        resources.add(resource);
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
        Set<Resource> resources = resourceService.filterResourcesByDatabaseId(databaseId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @GetMapping("/resources/library/{libraryId}")
    public String findByLibraryId(@PathVariable(value="libraryId") Long libraryId, Model model) {
        Set<Resource> resources = resourceService.filterResourcesByLibraryId(libraryId);
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @PostMapping("/resources")
    @ResponseBody
    public Resource create(@RequestBody Resource resource){
        return resourceService.save(resource);
    }

//    @PutMapping("/resources/{resourceId")
//    public ResponseEntity<Resource> updateResource(@PathVariable Long resourceId){
//        Optional<Resource> r = resourceService.findById(resourceId);
//        if()
//        if(Resource r = resourceService.findById(resourceId)){
//
//        }
//    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<Resource> deleteResource(@PathVariable Long resourceId) {
        return resourceService.deleteById(resourceId);
    }


}
