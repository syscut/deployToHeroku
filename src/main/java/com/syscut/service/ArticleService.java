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
		  
		return articleRepository.searchAllLike(str);
	}
//	public void insertArticle(Article article) {
//	Optional<Article> articleOptional = articleRepository.findArticleByTitle(article.getTitle());
//	if(articleOptional.isPresent()) {
//		throw new IllegalStateException("title already exsits");
//	}
//	articleRepository.save(article);
//	}
}	
//	@Value("${spring.datasource.url}")
//	  private String dbUrl;
//	 
//	@Autowired
//	 private DataSource dataSource;
	
//public List<NewArticle> getArticle(){
//	ObjectMapper objectMapper = new ObjectMapper();
//	StringBuffer sb = new StringBuffer();
//	  try (Connection connection = dataSource.getConnection()) {
//	      Statement stmt = connection.createStatement();
//	      ResultSet rs = stmt.executeQuery("select date,title from article");
//          sb.append("[");
//	      while (rs.next()) {
//	    	  sb.append("{\"date\":\""+String.valueOf(rs.getDate(1)).replaceAll("-", "/")+"\",\"title\":\""+rs.getString(2)+"\"},");
//	      }
//	      sb.deleteCharAt(sb.length()-1);
//	      sb.append("]");
//	    } catch (Exception e) {
//	      return null;
//	    }
//	  
//	  try {
//          return objectMapper.readValue(getClass().getClassLoader().getResourceAsStream(String.valueOf(sb)),
//                  new TypeReference<List<NewArticle>>() {
//                  });
//      } catch (Exception e) {
//    	  return null;
//      }

	
//	@Bean
//	  public DataSource dataSource() throws SQLException {
//	    if (dbUrl == null || dbUrl.isEmpty()) {
//	      return new HikariDataSource();
//	    } else {
//	      HikariConfig config = new HikariConfig();
//	      config.setJdbcUrl(dbUrl);
//	      return new HikariDataSource(config);
//	    }
//	  }
//}
