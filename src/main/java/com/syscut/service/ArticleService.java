package com.syscut.service;

import com.syscut.model.Article;
import com.syscut.model.ArticleRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleService {
	
	private final ArticleRepository articleRepository;
	StorageProperties properties = new StorageProperties();
	Path rootLocation = Paths.get(properties.getLocation());
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
	
	public void store(MultipartFile file[]) {
		try {
			if (file[0].isEmpty()) {
				throw new RuntimeException("無法上傳空檔案");
			}
			for(int i = 0;i <file.length;i++) {
			Path destinationFile = rootLocation.resolve(
					Paths.get(file[i].getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new RuntimeException("無法上傳檔案於目前資料夾外");
			}
			
			File targetFile = new File(destinationFile.toString());
			String fileName = destinationFile.getFileName().toString();
			String fileNameWithOutExtString = fileName.replaceFirst("[.][^.]+$", "");
			String fileExt = fileName.substring(fileName.lastIndexOf("."));
			int n = 1;
			while(targetFile.exists() && !targetFile.isDirectory()) {
				
				fileName = fileNameWithOutExtString + "(" + n + ")" + fileExt;
				targetFile.renameTo(new File(destinationFile.getParent()+"/"+fileName));
				n ++;
			}
			
			try (InputStream inputStream = file[i].getInputStream()) {
				Files.copy(inputStream, targetFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("上傳失敗", e);
		}
	}
	
	public Stream<Path> loadAll() {
		try {
			return Files.walk(rootLocation, 1)
				.filter(path -> !path.equals(rootLocation))
				.map(rootLocation::relativize);
		}
		catch (IOException e) {
			throw new RuntimeException("讀取失敗", e);
		}

	}
	
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new RuntimeException("無法讀取檔案：" + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("無法讀取檔案：" + filename, e);
		}
	}
	
	public void deleteAll(String filepath) {
		File delFile = new File(filepath);

		FileSystemUtils.deleteRecursively(delFile);
		//FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new RuntimeException("無法初始化上傳資料夾", e);
		}
	}

}	
