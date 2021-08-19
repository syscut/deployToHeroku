package com.syscut.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syscut.model.ReadArticle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleService {
	@Value("${spring.datasource.url}")
	  private String dbUrl;
	 
	@Autowired
	  private DataSource dataSource;
public List<ReadArticle> articleList(){
	ObjectMapper objectMapper = new ObjectMapper();
	StringBuffer sb = new StringBuffer();
	  try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      ResultSet rs = stmt.executeQuery("select date,title from article");
          sb.append("[");
	      while (rs.next()) {
	    	  sb.append("{\"date\":\""+String.valueOf(rs.getDate(1)).replaceAll("-", "/")+"\",\"title\":\""+rs.getString(2)+"\"},");
	      }
	      sb.deleteCharAt(sb.length()-1);
	      sb.append("]");
	    } catch (Exception e) {
	      return null;
	    }
	  
	  try {
          return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream(String.valueOf(sb)),
                  new TypeReference<List<ReadArticle>>() {
                  });
      } catch (Exception e) {
    	  return null;
      }
}
	
	@Bean
	  public DataSource dataSource() throws SQLException {
	    if (dbUrl == null || dbUrl.isEmpty()) {
	      return new HikariDataSource();
	    } else {
	      HikariConfig config = new HikariConfig();
	      config.setJdbcUrl(dbUrl);
	      return new HikariDataSource(config);
	    }
	  }
}
