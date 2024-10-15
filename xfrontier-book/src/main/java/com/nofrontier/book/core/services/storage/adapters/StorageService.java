package com.nofrontier.book.core.services.storage.adapters;

import org.springframework.web.multipart.MultipartFile;

import com.nofrontier.book.domain.exceptions.StorageServiceException;
import com.nofrontier.book.domain.model.Picture;

public interface StorageService {

	Picture save(MultipartFile file) throws StorageServiceException;

	void remove(String filename) throws StorageServiceException;

}