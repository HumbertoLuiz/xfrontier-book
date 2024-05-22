package com.nofrontier.book.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nofrontier.book.domain.services.ApiAddressService;
import com.nofrontier.book.dto.v1.requests.AddressRequest;
import com.nofrontier.book.dto.v1.responses.AddressResponse;
import com.nofrontier.book.utils.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses/v1")
public class AddressRestController {

    @Autowired
    private ApiAddressService apiAddressService;

	// ------------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Finds all Addresses", description = "Finds all Addresses", tags = { "Addresses" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponse.class))) }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content), })
    public ResponseEntity<PagedModel<EntityModel<AddressResponse>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "completeName"));
        return ResponseEntity.ok(apiAddressService.findAll(pageable));
    }

	// -------------------------------------------------------------------------------------------------------------------------------------------------

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
    @Operation(summary = "Finds a Address", description = "Finds a Address", tags = { "Address" }, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressResponse.class))),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content), })
    public ResponseEntity<EntityModel<AddressResponse>> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(apiAddressService.findById(id));
    }
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = { "http://localhost:8080", "https://nofrontier.com.br" })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML })
	@Operation(summary = "Adds a new Address", description = "Adds a new Address by passing in a JSON, XML or YML representation of the address!", tags = {
			"Books" }, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressResponse.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content), })
	public AddressResponse create(@RequestBody @Valid AddressRequest request) {
		var response = apiAddressService.create(request);
		return response.getContent();
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

    
    @PutMapping
    public AddressResponse update(@RequestBody @Valid AddressRequest request) {
        return apiAddressService.update(request);
    }

    @GetMapping
    public AddressResponse displayAddress() {
        return apiAddressService.displayAddress();
    }
}
