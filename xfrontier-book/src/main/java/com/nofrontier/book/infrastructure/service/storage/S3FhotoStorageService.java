//package com.nofrontier.book.infrastructure.service.storage;
//
//import java.net.URL;
//
//import org.springframework.stereotype.Service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.DeleteObjectRequest;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.nofrontier.book.core.services.storage.StorageProperties;
//import com.nofrontier.book.domain.services.StoragePictureService;
//
//@Service
//public class S3FhotoStorageService implements StoragePictureService {
//
//	AmazonS3 amazonS3;
//	StorageProperties storageProperties;
//
//	@Override
//	public PictureRecovered recover(String fileName) {
//		String filePath = getFilePath(fileName);
//
//		URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(),
//				filePath);
//
//		return PictureRecovered.builder().url(url.toString()).build();
//	}
//
//	@Override
//	public void store(NewPicture newPicture) {
//		try {
//			String filePath = getFilePath(newPicture.getFileName());
//
//			var objectMetadata = new ObjectMetadata();
//			objectMetadata.setContentType(newPicture.getContentType());
//
//			var putObjectRequest = new PutObjectRequest(
//					storageProperties.getS3().getBucket(), filePath,
//					newPicture.getInputStream(), objectMetadata)
//					.withCannedAcl(CannedAccessControlList.PublicRead);
//
//			amazonS3.putObject(putObjectRequest);
//		} catch (Exception e) {
//			throw new StorageException("Unable to send file to Amazon S3.", e);
//		}
//	}
//
//	@Override
//	public void remove(String fileName) {
//		try {
//			String filePath = getFilePath(fileName);
//
//			var deleteObjectRequest = new DeleteObjectRequest(
//					storageProperties.getS3().getBucket(), filePath);
//
//			amazonS3.deleteObject(deleteObjectRequest);
//		} catch (Exception e) {
//			throw new StorageException("Unable to delete file on Amazon S3.", e);
//		}
//	}
//
//	private String getFilePath(String fileName) {
//		return String.format("%s/%s",
//				storageProperties.getS3().getDirectoryPhotos(), fileName);
//	}
//
//}