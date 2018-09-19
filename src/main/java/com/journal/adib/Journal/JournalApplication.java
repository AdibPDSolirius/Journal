package com.journal.adib.Journal;

import com.journal.adib.Journal.Models.Language;
import com.journal.adib.Journal.Models.Resource;
import com.journal.adib.Journal.Repositories.LanguageRepository;
import com.journal.adib.Journal.Repositories.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class JournalApplication {

	public static void main(String[] args){

	    SpringApplication.run(JournalApplication.class, args);
	}
//
//	@Autowired
//	ResourceRepository resouceRepository;
//
//	@Autowired
//	LanguageRepository languageRepository;
//
//	@Transactional
//	@Override
//	public void run(String... arg0) throws Exception {
//		Resource web = new Resource();
//		web.setResourceName("Basic web tutorial");
//		web.setResourceURL("Link to learn web dev");
//		web.setId(new Long(1));
//
//		Resource spring = new Resource();
//		spring.setResourceName("Spring tutorial");
//		spring.setResourceURL("Link to learn spring");
//		spring.setId(new Long(2));
//
//		Language java = new Language();
//		java.setLanguageName("Java");
//		java.setId(new Long(1));
//
//		Language javascript = new Language();
//		javascript.setLanguageName("JavaScript");
//		javascript.setId(new Long(2));
//
//		languageRepository.save(java);
//		languageRepository.save(javascript);
//
//		Set<Language> webLanguages = new HashSet<>();
//		webLanguages.add(java);
//		webLanguages.add(javascript);
//
//		Set<Language> springLanguages = new HashSet<>();
//		springLanguages.add(java);
//
//		web.setLanguages(webLanguages);
//
//		spring.setLanguages(springLanguages);
//
//		resouceRepository.save(web);
//		resouceRepository.save(spring);
//
//		List<Resource> resourceList = resouceRepository.findAll();
//		List<Language> languageList = languageRepository.findAll();
//
//		System.out.println(resourceList.size());
//		System.out.println(languageList.size());
//
//
//
//	}


}
