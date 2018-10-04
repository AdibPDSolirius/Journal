package com.journal.adib.Journal;
import com.journal.adib.Journal.Models.*;
import com.journal.adib.Journal.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class JournalApplication{

	public static void main(String[] args){

	    SpringApplication.run(JournalApplication.class, args);
	}

}
