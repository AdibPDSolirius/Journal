package com.journal.adib.Journal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.journal.adib.Journal")
public class JournalApplication {


	public static void main(String[] args) {

	    SpringApplication.run(JournalApplication.class, args);
	}

}