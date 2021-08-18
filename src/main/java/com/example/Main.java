package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@SpringBootApplication  //說明這是一個Spring Boot
public class Main {
  
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
  
  @Value("${spring.datasource.url}")
  private String dbUrl;
 
  @Autowired
  private DataSource dataSource;
  
  public static void main(String[] args) throws Exception {
	  //啟動Spring
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String,Object> model) {
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      
	      ResultSet rs = stmt.executeQuery("select date,title from article");
	      ArrayList<String> href = new ArrayList<String>();
	      ArrayList<String> title = new ArrayList<String>();

	      while (rs.next()) {
	    	  href.add("/js-map?date="+String.valueOf(rs.getDate(1))+"&title="+rs.getString(2));
	    	  title.add(String.valueOf(rs.getDate(1)).replaceAll("-", "/")+"-"+rs.getString(2));
	      }
	      model.put("href", href);
	      model.put("title", title);
	      return "index";
	    } catch (Exception e) {
	    	model.put("message", e.getMessage());
	      return "error";
	    }
	  }
  
  @RequestMapping("/login")
  String doLoginValid(@Validated DoLogin doLogin,BindingResult bindingResult,Model model) {
	  if(bindingResult.hasErrors()) {
			return "login";
		}
	  return "index";
  }
  
  @RequestMapping("/newarticle")
  String article(Model model) {
	  model.addAttribute("newarticle",new NewArticle());
	  model.addAttribute("today",LocalDate.now().format(formatter));
	  return "new";
  }
  
  @PostMapping("/addarticle")
  String add(NewArticle newarticle,Model model) {
	  try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
	  stmt.executeUpdate("insert into article values ('"+LocalDate.now()+"','"+newarticle.getTitle()+"','{"+newarticle.getTags()+"}','"+newarticle.getContent()+"');");
	  }catch (Exception e) {
	      return newarticle.getDate()+newarticle.getContent()+newarticle.getTags()+newarticle.getTitle();
	    }
	  return "index";
  }

  @RequestMapping("/js-map")
  String jsmap(
		  @RequestParam(name="date")String date,
		  @RequestParam(name="title")String title,
		  Map<String, Object> model  
		  ) {
//	  if(today==null) {
//		  today = LocalDate.now().format(formatter);
//	  }
//	  model.put("today",today);
	  
	  try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      
	      ResultSet rs = stmt.executeQuery("SELECT date, title, tags, content FROM article where date = '"+date+"' and title = '"+title+"'");

	      ArrayList<String> output = new ArrayList<String>();
	      while (rs.next()) {
	        output.add(String.valueOf(rs.getDate(1)).replaceAll("-", "/"));
	        output.add(rs.getString(2));
	        output.add(String.valueOf(rs.getArray(3)));
	        output.add(rs.getString(4));
	      }

	      model.put("article", output);
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
