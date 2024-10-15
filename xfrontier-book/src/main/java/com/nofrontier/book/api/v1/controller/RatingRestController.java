package com.nofrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.ApiRatingService;
import com.nofrontier.book.dto.v1.RatingDto;
import com.nofrontier.book.dto.v1.MessageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books/{id}/ratings")
public class RatingRestController {

	@Autowired
	private ApiRatingService apiRatingservice;

	@PatchMapping
	public MessageResponse rateBook(
			@RequestBody @Valid RatingDto ratingRequest,
			@PathVariable Long id) {
		return apiRatingservice.rateBook(ratingRequest, id);
	}

}
