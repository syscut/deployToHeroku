package com.syscut.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.syscut.model.Article;
import com.syscut.model.DoLogin;
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
		String regx_code_f = "line-numbers\">\r\n";
		String regx_code_e = "</code>\r\n";
		String regx_pre_e = "</pre>\r\n";
		String article_be = article.getContent().replaceAll(regx_code_f, "line-numbers\">").replaceAll(regx_code_e, "</code>").replaceAll(regx_pre_e, "</pre>");
		String regex = "(<)(((?!([p][r][e]|[c][o][d][e]|[h][r][e][f]|[/][a]|[s][r][c]|[d][e][t][a][i][l][s]|[s][u][m][m][a][r][y]|[k][b][d]|[v][a][r]|[s][a][m][p]|[<>])).)*)(>)";
		String article_af = article_be.replaceAll(regex, "&lt;$2&gt;");
		article.setContent(article_af);
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
	public String searcharticle(Model model,String str) {
		List<Article> resault = articleService.getArticleBySearch(str);
		
        int s = resault.size();
		
		try {
			for(int i = 0;i < s;i++) {
				String c = resault.get(i).getContent().substring(0, 150);
				c += "...";
				resault.get(i).setContent(c);
			};
		} catch (Exception e) {}
		if(resault.isEmpty()) {
			model.addAttribute("err","查無結果");
		}else {
			model.addAttribute("err","");
		}
		model.addAttribute("article",resault);
		model.addAttribute("search_tag",str);
		  return "resault";
	  }
	
	  @RequestMapping("/go")
	  String login(Model model) {
		  model.addAttribute("doLogin", new DoLogin());
		  model.addAttribute("err","");
		return "login";
	  }
	  
	  @PostMapping("login")
	  String doLoginValid(@Validated DoLogin doLogin,BindingResult bindingResult,Model model) {
			  if(bindingResult.hasErrors()) {
				  model.addAttribute("err","err");
					return "login";
				}
			  model.addAttribute("login_name",doLogin.getId());
			  model.addAttribute("href",articleService.getAllArticle());
		  return "index";
	  }
}
