package com.journal.adib.Journal.Controllers;
import com.journal.adib.Journal.Models.Database;
import com.journal.adib.Journal.Services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
public class DatabaseController {

    @Autowired
    DatabaseService databaseService;

    @GetMapping("/databases")
    public String find(Model model) {
        List<Database> databases = (List<Database>) databaseService.findAll();
        model.addAttribute("databases",databases);
        return "database-list";
    }

    @PostMapping("/databases")
    @ResponseBody
    public Database create(@RequestBody Database database){
        return databaseService.save(database);
    }

    @DeleteMapping("/databases/{databaseId}")
    @ResponseBody
    public void deleteById(@PathVariable(value="databaseId") Long databaseId){
        databaseService.deleteById(databaseId);
    }
}