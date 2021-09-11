package com.syscut.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.syscut.model.Article;
import com.syscut.service.ArticleService;

@Controller
public class ArticleController {
	
	private final ArticleService articleService;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
	  @Autowired
	  public ArticleController(ArticleService articleService) {
		  this.articleService = articleService;
	  }
	
	@RequestMapping("/")
	public String getArticle(Model model){
		model.addAttribute("href",articleService.getAllArticle());
		return "index";
	}
	
	  @RequestMapping("/js-map")
	 public String jsmap(
			  @RequestParam(name="id")Integer id,
			  Model model  
			  ) {
//		  if(today==null) {
//			  today = LocalDate.now().format(formatter);
//		  }
//		  model.put("today",today);
		  List<Article> l = articleService.getArticleById(id);
		  List<String> t = Arrays.asList(l.get(0).getTags());
		  //t = t.replace("[", "").replace("]", "").replace(",", "��");
		  System.out.print(l.get(0).getContent());
		  model.addAttribute("article",l);
		  model.addAttribute("tag",t);
		  return "js-map";
	  }
	  
	  @RequestMapping("/newarticle")
	  public String newarticle(Model model) {
		  model.addAttribute("article",new Article());
		  model.addAttribute("today",LocalDate.now().format(formatter));
		  return "new";
	  }  
	
	@PostMapping("addarticle")
	public String insertarticle(Article article,Model model) {
		  article.setDate(LocalDate.now());
		  
		  articleService.insert(article);
		  model.addAttribute("href",articleService.getAllArticle());
		  return "index";
	  }
	
	
	@RequestMapping("/tags")
	  public String tags(@RequestParam(name="tag")String tag,
			  Model model) {
		List<Article> a = articleService.getArticleByTag(tag);
		int s = a.size();
		
		try {
			for(int i = 0;i < s;i++) {
				String c = a.get(i).getContent().substring(0, 150);
				c += "...";
				a.get(i).setContent(c);
			};
		} catch (Exception e) {}
		if(a.isEmpty()) {
			model.addAttribute("err","查無結果");
		}else {
			model.addAttribute("err","");
		}
		  model.addAttribute("article",a);
		  model.addAttribute("search_tag",tag);
		  return "resault";
	  }  

	@PostMapping("search")
	public String searcharticle(String str) {
		  
		  return str;
	  }
	
	  
	//  String index(Model model) {
//		  model.addAttribute("href",articleService.articleList());
//		  
//		  return "index";
//		    try (Connection connection = dataSource.getConnection()) {
//		      Statement stmt = connection.createStatement();
//		      
//		      ResultSet rs = stmt.executeQuery("select date,title from article");
//		      ArrayList<String> href = new ArrayList<String>();
		      //ArrayList<String> title = new ArrayList<String>();
	          //StringBuffer sb = new StringBuffer();
	          //sb.append("[");
//		      while (rs.next()) {
//		    	  href.add("/js-map?date="+String.valueOf(rs.getDate(1))+"&title="+rs.getString(2));
		    	  //title.add(String.valueOf(rs.getDate(1)).replaceAll("-", "/")+"-"+rs.getString(2));
		    	  //sb.append("{\"date\":\""+String.valueOf(rs.getDate(1)).replaceAll("-", "/")+"\",\"title\":\""+rs.getString(2)+"\"},");
		      //}
		      //sb.deleteCharAt(sb.length()-1);
		      //sb.append("]");
		      //model.put("href", sb);
//		      model.put("href", href);
//		      return "index";
//		    } catch (Exception e) {
//		    	model.put("message", e.getMessage());
//		      return "error";
//		    }
		  
	  
//	  @RequestMapping("/login")
//	  String doLoginValid(@Validated DoLogin doLogin,BindingResult bindingResult,Model model) {
//		  if(bindingResult.hasErrors()) {
//				return "login";
//			}
//		  return "index";
//	  }
	  
	  
//	  
//	  @PostMapping("/addarticle")
//	  String add(NewArticle newarticle,Model model) {
//		  try(Connection connection = dataSource.getConnection()){
//	      Statement stmt = connection.createStatement();
//		  stmt.executeUpdate("insert into article values ('"+LocalDate.now()+"','"+newarticle.getTitle()+"','{"+newarticle.getTags()+"}','"+newarticle.getContent()+"');");
//		  }catch (Exception e) {
//		      return newarticle.getDate()+newarticle.getContent()+newarticle.getTags()+newarticle.getTitle();
//		    }
//		  return "index";
//	  }



//	  @RequestMapping("/db")
//	  String db(Map<String, Object> model) {
//	    try (Connection connection = dataSource.getConnection()) {
//	      Statement stmt = connection.createStatement();
//	      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//	      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//	      
//	      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
//	      ArrayList<String> output = new ArrayList<String>();
//	      while (rs.next()) {
//	        output.add("Read from DB: " + rs.getTimestamp("tick"));
//	      }
//
//	      model.put("records", output);
//	      return "db";
//	    } catch (Exception e) {
//	      model.put("message", e.getMessage());
//	      return "error";
//	    }
//	  }

//	  @Bean
//	  public DataSource dataSource() throws SQLException {
//	    if (dbUrl == null || dbUrl.isEmpty()) {
//	      return new HikariDataSource();
//	    } else {
//	      HikariConfig config = new HikariConfig();
//	      config.setJdbcUrl(dbUrl);
//	      return new HikariDataSource(config);
//	    }
//	  }

}
