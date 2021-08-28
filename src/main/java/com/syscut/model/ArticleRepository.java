package com.syscut.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer>{

	List<Article> findAllById(Integer i);
	
//	@SQLInsert(sql = "")
//	List<Article> findArticleByTitleLike(String title);
////  @Query(value = "select date,title,content,tags from article where title like %?1%")
//  List<Article> findArticleByTitleLike(String title);
  
//  @Query(value = "insert into article (date,title,content,tags) values (?,?,?,?)")
//  Article insertArticle(LocalDate date,String title,String content,String[] tags);
}
