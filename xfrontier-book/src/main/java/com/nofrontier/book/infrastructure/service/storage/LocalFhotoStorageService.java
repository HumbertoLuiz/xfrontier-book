//package com.nofrontier.book.infrastructure.service.storage;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//import org.springframework.stereotype.Service;
//import org.springframework.util.FileCopyUtils;
//
//import com.nofrontier.book.core.services.storage.StorageProperties;
//import com.nofrontier.book.domain.services.StoragePictureService;
//
//@Service
//public class LocalFhotoStorageService implements StoragePictureService {
//
//	StorageProperties storageProperties;
//
//	@Override
//	public PictureRecovered recover(String fileName) {
//		try {
//			Path filePath = getFilePath(fileName);
//
//			return PictureRecovered.builder()
//					.inputStream(Files.newInputStream(filePath)).build();
//		} catch (Exception e) {
//			throw new StorageException("Could not recover file.", e);
//		}
//	}
//
//	@Override
//	public void store(NewPicture newPicture) {
//		try {
//			Path filePath = getFilePath(newPicture.getFileName());
//
//			FileCopyUtils.copy(newPicture.getInputStream(),
//					Files.newOutputStream(filePath));
//		} catch (Exception e) {
//			throw new StorageException("Could not store file.", e);
//		}
//	}
//
//	@Override
//	public void remove(String fileName) {
//		try {
//			Path filePath = getFilePath(fileName);
//
//			Files.deleteIfExists(filePath);
//		} catch (Exception e) {
//			throw new StorageException("Could not delete file.", e);
//		}
//	}
//
//	private Path getFilePath(String fileName) {
//		return storageProperties.getLocal().getDirectoryPhotos()
//				.resolve(Path.of(fileName));
//	}
//
//}