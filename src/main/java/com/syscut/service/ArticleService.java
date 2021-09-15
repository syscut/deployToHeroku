package com.syscut.service;

import com.syscut.model.Article;
import com.syscut.model.ArticleRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleService {
	
	private final ArticleRepository articleRepository;
	
	@Autowired
	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}
	
	public List<Article> getAllArticle(){
		  return articleRepository.findAllByOrderByIdDesc();
	}
	
	public List<Article> getArticleById(Integer i){
		  
		return articleRepository.findAllById(i);
	}
	
	public Article insert(Article article){
		  
		return articleRepository.save(article);
	}

	public List<Article> getArticleByTag(String tag){
		  
		return articleRepository.findTagsLike(tag);
	}
	
	public List<Article> getArticleBySearch(String str){
		  str = str.trim();
		return articleRepository.searchAllLike(str);
	}
	
	public List<String> allTags(){
		return articleRepository.getAllTags();
	}
	
	public List<String> countTags(){
	    return articleRepository.countAllTags();
	}

}	
