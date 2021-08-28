package com.syscut.model;

import java.time.LocalDate;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;




@Entity
@Table
public class Article extends BaseEntity{
	  
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;
	  
	  private LocalDate date;
	  private String title;
	  private String content;
	  
	  @Type(type = "string-array")
	  @Column(name = "tags",columnDefinition = "text[]")
	  private String[] tags;
    
	  public Article() {}
	  public Article(LocalDate date,String title,String content,String[] tags) {
		  
		  this.date = date;
		  this.title = title;
		  this.content = content;
		  this.tags = tags;
	  }
	  
	  public Integer getId() {
		    return id;
		  }

		  public void setId(Integer id) {
		    this.id = id;
		  }
		  
	  public LocalDate getDate() {
	    return date;
	  }

	  public void setDate(LocalDate date) {
	    this.date = date;
	  }
	  
	  public String getTitle() {
		    return title;
		  }

	  public void setTitle(String title) {
		    this.title = title;
		  }
	  public String getContent() {
		    return content;
		  }

	  public void setContent(String content) {
		    this.content = content;
		  }
	  public String[] getTags() {
		    return tags;
		  }

	  public void setTags(String[] tags) {
		    this.tags = tags;
		  }
	  @Override
	  public String toString() {
		  return "Article{id=\'"+id+"\',date=\'"+date+"\',title=\'"+title+"\',content=\'"+content+"\',tags=\'"+tags+"\'}";
	  
	  }
}
