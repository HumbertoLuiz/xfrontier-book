package com.nofrontier.book.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nofrontier.book.domain.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

	Optional<Picture> findByFilename(String filename);
}
