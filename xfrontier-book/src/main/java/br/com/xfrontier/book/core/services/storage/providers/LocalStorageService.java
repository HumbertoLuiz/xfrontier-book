package br.com.xfrontier.book.core.services.storage.providers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.xfrontier.book.api.v1.controller.StorageRestController;
import br.com.xfrontier.book.core.services.storage.adapters.StorageService;
import br.com.xfrontier.book.domain.exceptions.StorageServiceException;
import br.com.xfrontier.book.domain.model.Picture;
import br.com.xfrontier.book.domain.repository.PictureRepository;

@Service
//@Profile("dev")
public class LocalStorageService implements StorageService {

    private final Path pastaUpload = Paths.get("uploads");

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public Picture save(MultipartFile file) throws StorageServiceException {
        try {
            return trySave(file);
        } catch (IOException exception) {
            throw new StorageServiceException(exception.getLocalizedMessage());
        }
    }

    @Override
    public void remove(String filename) throws StorageServiceException {
        var arquivo = pastaUpload.resolve(filename);

        try {
            Files.deleteIfExists(arquivo);
        } catch (IOException exception) {
            throw new StorageServiceException(exception.getLocalizedMessage());
        }
    }

    public Resource findPicture(String filename) {
        var arquivo = pastaUpload.resolve(filename);

        try {
            return new UrlResource(arquivo.toUri());
        } catch (MalformedURLException e) {
            throw new StorageServiceException(e.getLocalizedMessage());
        }
    }

    private Picture trySave(MultipartFile file) throws IOException {
        if (!Files.exists(pastaUpload)) {
            Files.createDirectories(pastaUpload);
        }

        var picture = generateModelPicture(file);
        Files.copy(file.getInputStream(), pastaUpload.resolve(picture.getFilename()));
        return pictureRepository.save(picture);
    }

    private Picture generateModelPicture(MultipartFile file) throws IOException {
        var picture = new Picture();
        var filename = generateNewFilename(file.getOriginalFilename());
        var url = linkTo(methodOn(StorageRestController.class).findPicture(filename)).toString();
        picture.setFilename(filename);
        picture.setContentLength(file.getSize());
        picture.setContentType(file.getContentType());
        picture.setUrl(url);
        return picture;
    }

    private String generateNewFilename(String filenameOriginal) {
        var ext = filenameOriginal.split("\\.")[1];
        var filename = UUID.randomUUID().toString() + "." + ext;
        return filename;
    }

}