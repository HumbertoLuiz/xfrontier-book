//package br.com.xfrontier.book.core.services.storage;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import br.com.xfrontier.book.core.services.storage.StorageProperties.StorageType;
//import br.com.xfrontier.book.domain.services.StoragePictureService;
//import br.com.xfrontier.book.infrastructure.service.storage.LocalFhotoStorageService;
//import br.com.xfrontier.book.infrastructure.service.storage.S3FhotoStorageService;
//
//@Configuration
//public class StorageConfig {
//
//	@Autowired
//	private StorageProperties storageProperties;
//	
//	@Bean
//	@ConditionalOnProperty(name = "nofrontier.storage.type", havingValue = "s3")
//	AmazonS3 amazonS3() {
//		var credentials = new BasicAWSCredentials(
//				storageProperties.getS3().getIdAccessKey(), 
//				storageProperties.getS3().getKeySecretAccess());
//		
//		return AmazonS3ClientBuilder.standard()
//				.withCredentials(new AWSStaticCredentialsProvider(credentials))
//				.withRegion(storageProperties.getS3().getRegion())
//				.build();
//	}
//	
//	@Bean
//	StoragePictureService fotoStorageService() {
//		if (StorageType.S3.equals(storageProperties.getType())) {
//			return new S3FhotoStorageService();
//		} else {
//			return new LocalFhotoStorageService();
//		}
//	}
//	
//}
