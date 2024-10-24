package br.com.xfrontier.book.api.v1.controller;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.xfrontier.book.dto.v1.UploadFileResponseDto;
import br.com.xfrontier.book.domain.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = {"http://localhost:8080", "https://xfrontier.com.br"})
@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileRestController {
	
	private Logger logger = Logger.getLogger(FileRestController.class.getName());
	
	@Autowired
	private FileStorageService service;
	
	@PostMapping("/uploadFile")
	public UploadFileResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("Storing file to disk");
		
		var filename = service.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/file/v1/downloadFile/")
			.path(filename)
			.toUriString();
		
		return
			new UploadFileResponseDto(
				filename, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponseDto> uploadMultipleFiles(
		@RequestParam("files") MultipartFile[] files) {
		logger.info("Storing files to disk");
		
		return Arrays.asList(files)
			.stream()
			.map(file -> uploadFile(file))
			.collect(Collectors.toList());
	}
	
	//MY_file.txt
	@GetMapping("/downloadFile/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(
		@PathVariable String filename, HttpServletRequest request) {
		
		logger.info("Reading a file on disk");
		
		Resource resource = service.loadFileAsResource(filename);
		String contentType = "";
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			logger.info("Could not determine file type!");
		}
		
		if (contentType.isBlank()) contentType = "application/octet-stream";
		
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header(
				HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resource.getFilename() + "\"")
			.body(resource);
	}
}
