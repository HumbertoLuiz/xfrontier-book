package br.com.xfrontier.book.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.xfrontier.book.domain.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {

	Optional<Picture> findByFilename(String filename);
}
