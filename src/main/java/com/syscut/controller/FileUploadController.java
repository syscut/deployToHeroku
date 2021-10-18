package com.syscut.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.syscut.service.ArticleService;
import com.syscut.service.StorageProperties;


@Controller
public class FileUploadController {
	
	private final ArticleService articleService;

	StorageProperties properties = new StorageProperties();
	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	@Autowired
	public FileUploadController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@RequestMapping("/uploadfiles")
	public String listUploadedFiles(Model model) throws IOException {
		
		
		model.addAttribute("files", articleService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));
       
        model.addAttribute("names",articleService.loadAll().map(
				path -> path.getFileName().toString())
				.collect(Collectors.toList()));
        
        model.addAttribute("info",articleService.loadAll().map(
				path -> {
					try {
						Date dt = new Date(articleService.loadAsResource(path.toString()).lastModified());
						return sdFormat.format(dt);
					} catch (IOException e) {
						return e;
					}
				}).collect(Collectors.toList()));
        
		return "upload";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {

		Resource file = articleService.loadAsResource(filename);
		String fileName = file.getFilename();
		MediaType mediaType = MediaType.TEXT_HTML;
		String fileExt = fileName.substring(fileName.lastIndexOf("."));
		
		if(fileExt.equals(".pdf")) {
			mediaType =  MediaType.APPLICATION_PDF;
		}else if(fileExt.equals(".jpeg")||fileExt.equals(".bmp")||fileExt.equals(".jpg")||fileExt.equals(".jpe")||fileExt.equals(".tif")||fileExt.equals(".tiff")) {
			mediaType = MediaType.IMAGE_JPEG;
		}else if(fileExt.equals(".png")) {
			mediaType = MediaType.IMAGE_PNG;
		}else if(fileExt.equals(".gif")) {
			mediaType = MediaType.IMAGE_GIF;
		}
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"inline; filename="+fileName).contentType(mediaType).body(file);
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("file") MultipartFile file[],
			RedirectAttributes redirectAttributes) {
      
		try {
			articleService.store(file);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message",e.getMessage());
			return "redirect:/uploadfiles";
		}
		String fileName = "";
		String comma = "";
		for(int i = 0;i<file.length;i++) {
			fileName += comma+file[i].getOriginalFilename();
			comma = "、 ";
		}
		redirectAttributes.addFlashAttribute("message",
				"檔案" + fileName + "上傳成成功!");

		return "redirect:/uploadfiles";
	}
	
	@GetMapping("/del/{filename}")
	public String delFile(@PathVariable String filename,RedirectAttributes redirectAttributes) {

		//Resource file = storageService.loadAsResource(filename);
		articleService.deleteAll(properties.getLocation()+"/"+filename);
		//model.addAttribute("message",properties.getLocation());
		redirectAttributes.addFlashAttribute("message",filename+"已刪除");
		return "redirect:/uploadfiles";
		
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

}