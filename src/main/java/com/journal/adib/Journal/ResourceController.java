package com.journal.adib.Journal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ResourceController {


        @GetMapping("/resources")
        public String greeting(Model model) {
//            List<Resource> resources = new ArrayList();
//            Resource r = new Resource();
//            r.setResourceName("Spring tutorial");
//            r.setResourceURL("https://www.tutorialspoint.com/spring/");
//            resources.add(r);
//            ResourceService rs = new ResourceService();
            model.addAttribute("resources", "hello");
            return "resource-list";

        }

//        @PutMapping("/resource/{resourceId}")
//        public Resource updateResource(){
//
//        }


}
