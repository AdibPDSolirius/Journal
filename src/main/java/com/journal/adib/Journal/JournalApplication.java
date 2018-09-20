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
public class JournalApplication implements CommandLineRunner{

	public static void main(String[] args){

	    SpringApplication.run(JournalApplication.class, args);
	}

	@Autowired
    ResourceRepository resourceRepository;

	@Autowired
    LanguageRepository languageRepository;

	@Autowired
    FrameworkRepository frameworkRepository;

	@Autowired
    DatabaseRepository databaseRepository;

	@Autowired
    LibraryRepository libraryRepository;

	@Transactional
	@Override
	public void run(String... arg0) throws Exception {
		Language java = new Language();
		java.setName("Java");
		java.setId(new Long(1));

		Language javascript = new Language();
		javascript.setName("JavaScript");

		languageRepository.save(java);
		languageRepository.save(javascript);
		java.setId(new Long(2));


		Framework angular = new Framework();
		angular.setName("Angulars");
		angular.setId(new Long(30));

		Framework express = new Framework();
		express.setName("Express");
		express.setId(new Long(31));

		frameworkRepository.save(angular);
		frameworkRepository.save(express);

		Database mongoDB = new Database();
		mongoDB.setName("mongoDB");

		Database postgresql = new Database();
		postgresql.setName("postgreSQL");

		databaseRepository.save(mongoDB);
		databaseRepository.save(postgresql);

		Library react = new Library();
		react.setName("reacts");

		Library hadoop = new Library();
		hadoop.setName("Hadoop");

		libraryRepository.save(react);
		libraryRepository.save(hadoop);

		Resource web = new Resource();
		web.setName("Basic web tutorial");
		web.setUrl("Link to learn web dev");
		web.setId(new Long(3));

		Resource spring = new Resource();
		spring.setName("Spring tutorial");
		spring.setUrl("Link to learn spring");
		spring.setId(new Long(4));

		Set<Language> webLanguages = new HashSet<>();
		webLanguages.add(java);
		webLanguages.add(javascript);

		Set<Framework> webFrameworks = new HashSet<>();
		webFrameworks.add(express);
		webFrameworks.add(angular);

		Set<Library> webLibraries = new HashSet<>();
		webLibraries.add(react);

		Set<Database> webDatabases = new HashSet<>();
		webDatabases.add(mongoDB);
		webDatabases.add(postgresql);

		Set<Language> springLanguages = new HashSet<>();
		springLanguages.add(java);

		web.setLanguages(webLanguages);
		web.setFrameworks(webFrameworks);
		web.setDatabases(webDatabases);
		web.setLibraries(webLibraries);

		spring.setLanguages(springLanguages);

		resourceRepository.save(web);
		resourceRepository.save(spring);

	}



}
