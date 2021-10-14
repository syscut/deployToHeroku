//package com.syscut.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.stream.Stream;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.util.FileSystemUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//public class FileSystemStorageService implements StorageService{
//	
//	private final Path rootLocation;
//
//	@Autowired
//	public FileSystemStorageService(StorageProperties properties) {
//		this.rootLocation = Paths.get(properties.getLocation());
//	}
//
//	@Override
//	public void store(MultipartFile file) {
//		try {
//			if (file.isEmpty()) {
//				throw new RuntimeException("�L�k�W�Ǫ��ɮ�");
//			}
//			Path destinationFile = this.rootLocation.resolve(
//					Paths.get(file.getOriginalFilename()))
//					.normalize().toAbsolutePath();
//			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
//				// This is a security check
//				throw new RuntimeException("�L�k�W���ɮש�ثe��Ƨ��~");
//			}
//			try (InputStream inputStream = file.getInputStream()) {
//				Files.copy(inputStream, destinationFile,
//					StandardCopyOption.REPLACE_EXISTING);
//			}
//		}
//		catch (IOException e) {
//			throw new RuntimeException("�W�ǥ���", e);
//		}
//	}
//
//	@Override
//	public Stream<Path> loadAll() {
//		try {
//			return Files.walk(this.rootLocation, 1)
//				.filter(path -> !path.equals(this.rootLocation))
//				.map(this.rootLocation::relativize);
//		}
//		catch (IOException e) {
//			throw new RuntimeException("Ū������", e);
//		}
//
//	}
//
//	@Override
//	public Path load(String filename) {
//		return rootLocation.resolve(filename);
//	}
//
//	@Override
//	public Resource loadAsResource(String filename) {
//		try {
//			Path file = load(filename);
//			Resource resource = new UrlResource(file.toUri());
//			if (resource.exists() || resource.isReadable()) {
//				return resource;
//			}
//			else {
//				throw new RuntimeException("�L�kŪ���ɮסG" + filename);
//
//			}
//		}
//		catch (MalformedURLException e) {
//			throw new RuntimeException("�L�kŪ���ɮסG" + filename, e);
//		}
//	}
//
//	@Override
//	public void deleteAll(String filepath) {
//		File delFile = new File(filepath);
//
//		FileSystemUtils.deleteRecursively(delFile);
//		//FileSystemUtils.deleteRecursively(rootLocation.toFile());
//	}
//
//	@Override
//	public void init() {
//		try {
//			Files.createDirectories(rootLocation);
//		}
//		catch (IOException e) {
//			throw new RuntimeException("�L�k��l�ƤW�Ǹ�Ƨ�", e);
//		}
//	}
//}
