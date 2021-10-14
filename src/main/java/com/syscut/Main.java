package com.syscut;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.syscut.service.ArticleService;
import com.syscut.service.StorageProperties;


@SpringBootApplication  
@EnableConfigurationProperties(StorageProperties.class)
public class Main {
 
  public static void main(String[] args) throws Exception {
	  
    SpringApplication.run(Main.class, args);
  }
  
  @Bean
	CommandLineRunner init(ArticleService articleService) {
		return (args) -> {
			articleService.init();
		};
	}
  
}
