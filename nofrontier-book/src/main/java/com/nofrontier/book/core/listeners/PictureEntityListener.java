package com.nofrontier.book.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nofrontier.book.core.services.storage.adapters.StorageService;
import com.nofrontier.book.domain.model.Picture;

import jakarta.persistence.PreRemove;

@Component
public class PictureEntityListener {

    @Autowired
    private static StorageService storageService;


    public void setStorageService(StorageService storageService) {
        PictureEntityListener.storageService = storageService;
    }

    @PreRemove
    private void preRemove(Picture picture) {
        storageService.remove(picture.getFilename());
    }

}
