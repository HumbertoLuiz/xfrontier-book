package br.com.xfrontier.book.core.services.storage.adapters;

import org.springframework.web.multipart.MultipartFile;

import br.com.xfrontier.book.domain.exceptions.StorageServiceException;
import br.com.xfrontier.book.domain.model.Picture;

public interface StorageService {

	Picture save(MultipartFile file) throws StorageServiceException;

	void remove(String filename) throws StorageServiceException;

}