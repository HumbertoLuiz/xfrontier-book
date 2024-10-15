//package com.nofrontier.book.core.services.storage;
//
//import java.nio.file.Path;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import com.amazonaws.regions.Regions;
//
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Component
//@ConfigurationProperties("nofrontier.storage")
//public class StorageProperties {
//
//	private Local local = new Local();
//	private S3 s3 = new S3();
//	private StorageType type = StorageType.LOCAL;
//	
//	public enum StorageType {
//		
//		LOCAL, S3
//		
//	}
//	
//	@Getter
//	@Setter
//	public class Local {
//		
//		private Path directoryPhotos;
//		
//	}
//	
//	@Getter
//	@Setter
//	public class S3 {
//		
//		private String idAccessKey;
//		private String keySecretAccess;
//		private String bucket;
//		private Regions region;
//		private String directoryPhotos;
//		
//	}
//	
//}
