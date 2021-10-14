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
	
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("�L�k�W�Ǫ��ɮ�");
			}
			Path destinationFile = rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new RuntimeException("�L�k�W���ɮש�ثe��Ƨ��~");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("�W�ǥ���", e);
		}
	}
	
	public Stream<Path> loadAll() {
		try {
			return Files.walk(rootLocation, 1)
				.filter(path -> !path.equals(rootLocation))
				.map(rootLocation::relativize);
		}
		catch (IOException e) {
			throw new RuntimeException("Ū������", e);
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
				throw new RuntimeException("�L�kŪ���ɮסG" + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("�L�kŪ���ɮסG" + filename, e);
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
			throw new RuntimeException("�L�k��l�ƤW�Ǹ�Ƨ�", e);
		}
	}

}	
