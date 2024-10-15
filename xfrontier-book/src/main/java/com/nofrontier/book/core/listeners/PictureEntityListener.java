package com.nofrontier.book.core.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.nofrontier.book.core.services.storage.adapters.StorageService;
import com.nofrontier.book.domain.model.Picture;

import jakarta.persistence.PreRemove;

@Component
public class PictureEntityListener {

    @Autowired
    @Lazy
    private StorageService storageService;

    @PreRemove
    private void preRemove(Picture picture) {
        // Check that the storageService is null before calling the remove method
        if (storageService != null) {
            storageService.remove(picture.getFilename());
        } else {
            throw new IllegalStateException("StorageService is not initialized.");
        }
    }
}


