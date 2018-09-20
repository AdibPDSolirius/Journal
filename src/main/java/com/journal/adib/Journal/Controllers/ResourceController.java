package com.journal.adib.Journal.Controllers;

import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


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
