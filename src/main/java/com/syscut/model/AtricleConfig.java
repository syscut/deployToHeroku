package com.syscut.model;

import java.sql.Array;
import java.time.LocalDate;
import java.util.LinkedList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AtricleConfig {
	
@Bean
CommandLineRunner commandLineRunner(ArticleRepository repository) {
	
	//String[] tags = {"a","b"};
	 
	return args->{
//		Article a = new Article(
//				LocalDate.now(),"test03","3",tags
//				);
		//repository.save(a);
	};
}
}
