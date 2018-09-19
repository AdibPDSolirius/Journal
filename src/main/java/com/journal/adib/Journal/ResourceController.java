package com.journal.adib.Journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ResourceController {

    @Autowired
    ResourceService resourceService;


    @GetMapping("/resources")
    public String findResources(Model model) {
        List<Resource> resources = resourceService.findAll();
        model.addAttribute("resources",resources);
        return "resource-list";

    }

    @PostMapping("/resources")
    @ResponseBody
    public Resource createResource(@RequestBody Resource resource){
        return resourceService.saveResource(resource);
    }


}
