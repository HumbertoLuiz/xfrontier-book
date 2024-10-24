package br.com.xfrontier.book.api.v1.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.core.services.storage.providers.LocalStorageService;
import br.com.xfrontier.book.domain.repository.PictureRepository;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080", "https://xfrontier.com.br"})
@RestController
@RequestMapping("/uploads")
//@Profile("dev")
public class StorageRestController {

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private LocalStorageService localStorageService;

    @GetMapping
    public ResponseEntity<Object> findPicture(@RequestParam String filename) throws IOException {
        var picture = pictureRepository.findByFilename(filename).get();
        var file = localStorageService.findPicture(filename);

        return ResponseEntity.ok()
            .header("Content-Type", picture.getContentType())
            .header("Content-Length", picture.getContentLength().toString())
            .body(file.getInputStream().readAllBytes());
    }

}

