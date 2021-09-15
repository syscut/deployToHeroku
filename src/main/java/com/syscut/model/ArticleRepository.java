package com.syscut.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer>{

	List<Article> findAllById(Integer i);
	List<Article> findAllByOrderByIdDesc();
//	@SQLInsert(sql = "")
//	List<Article> findArticleByTitleLike(String title);
////  @Query(value = "select date,title,content,tags from article where title like %?1%")
//  List<Article> findArticleByTitleLike(String title);
  
 @Query(value = "select * from article where ?1 = ANY(tags) order by id desc", nativeQuery=true)
  List<Article> findTagsLike(String t);
 
 @Query(value = "select * from article where ?1 ilike ANY(tags) or title ilike %?1% or content ilike %?1%", nativeQuery=true)
 List<Article> searchAllLike(String t);
 
 @Query(value ="select distinct unnest(tags) from article", nativeQuery=true)
 List<String> getAllTags();
 
 @Query(value = "select tag, count(*) tag_no from article a cross join lateral unnest(a.tags) tag group by tag order by tag_no desc", nativeQuery=true)
 List<String> countAllTags();
}
