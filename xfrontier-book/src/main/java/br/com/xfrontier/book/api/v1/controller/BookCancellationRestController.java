package br.com.xfrontier.book.api.v1.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.services.ApiBookCancellationService;
import br.com.xfrontier.book.dto.v1.BookCancellationRequest;
import br.com.xfrontier.book.dto.v1.MessageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books/{id}/cancel/v1")
public class BookCancellationRestController {

    private final ApiBookCancellationService service;

    public BookCancellationRestController(ApiBookCancellationService service) {
        this.service = service;
    }

    @PatchMapping
    public MessageResponse cancel(@PathVariable Long id,
                                  @RequestBody @Valid BookCancellationRequest request) {
        return service.cancel(id, request);
    }
}

