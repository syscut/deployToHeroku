package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication  //說明這是一個Spring Boot
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;
 
  @Autowired
  private DataSource dataSource;
  
  public static void main(String[] args) throws Exception {
	  //啟動Spring
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }
  
  @RequestMapping("/js-map")
  String jsmap(
		  @RequestParam(name="today", required = false)String today,Map<String, Object> model  
		  ) {
	  if(today==null) {
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		  today = LocalDate.now().format(formatter);
	  }
	  model.put("today",today);
	  
	  try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      
	      ResultSet rs = stmt.executeQuery("SELECT * FROM article");

	      ArrayList<String> output = new ArrayList<String>();
	      while (rs.next()) {
	        output.add(String.valueOf(rs.getDate(1)));
	        output.add(rs.getString(2));
	        output.add(String.valueOf(rs.getArray(3)));
	        output.add(rs.getString(4));
	      }

	      model.put("records", output);
	      return "js-map";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
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
