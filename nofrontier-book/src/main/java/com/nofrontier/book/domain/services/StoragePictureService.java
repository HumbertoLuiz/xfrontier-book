package com.nofrontier.book.domain.services;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

public interface StoragePictureService {

	PictureRecovered recover(String fileName);
	
	void store(NewPicture newPicture);
	
	void remove(String fileName);
	
	default void replace(String oldFileName, NewPicture newPicture) {
		this.store(newPicture);
		
		if (oldFileName != null) {
			this.remove(oldFileName);
		}
	}
	
	default String generateFileName(String originalName) {
		return UUID.randomUUID().toString() + "_" + originalName;
	}
	
	@Builder
	@Getter
	class NewPicture {
		
		private String fileName;
		private String contentType;
		private InputStream inputStream;
		
	}
	
	@Builder
	@Getter
	class PictureRecovered {
		
		private InputStream inputStream;
		private String url;
		
		public boolean hasUrl() {
			return url != null;
		}
		
		public boolean temInputStream() {
			return inputStream != null;
		}
		
	}
	
}
