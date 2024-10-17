package br.com.xfrontier.book.api.v1.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.xfrontier.book.domain.services.ApiAddressService;
import br.com.xfrontier.book.dto.v1.AddressDto;
import br.com.xfrontier.book.utils.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/addresses/v1")
@Tag(name = "Addresses", description = "Endpoints for Managing Addresses")
public class AddressRestController {

	private final ApiAddressService apiAddressService;

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a Address", description = "Finds a Address", tags = {
			"Addresses"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressDto.class))),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public AddressDto findById(@PathVariable(value = "id") Long id) {
		return apiAddressService.findById(id);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping(produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds all Addresses", description = "Finds all Addresses", tags = {
			"Addresses"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<PagedModel<EntityModel<AddressDto>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC
				: Direction.ASC;

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDirection, "street"));
		return ResponseEntity.ok(apiAddressService.findAll(pageable));
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = {"http://localhost:8080",
			"https://xfrontier.com.br"})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Adds a new Address", description = "Adds a new Address by passing in a JSON, XML or YML representation of the address!", tags = {
			"Addresses"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressDto.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public AddressDto create(
			@RequestBody @Valid AddressDto addressDtoRequest) {
		return apiAddressService.create(addressDtoRequest);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a Address", description = "Updates a Address by passing in a JSON, XML or YML representation of the address!", tags = {
			"Addresses"}, responses = {
					@ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = AddressDto.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public AddressDto update(@PathVariable(value = "id") Long id, @RequestBody @Valid AddressDto addressDtoRequest) {
		return apiAddressService.update(id, addressDtoRequest);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a Address", description = "Deletes a Address by passing in a JSON, XML or YML representation of the address!", tags = {
			"Addresses"}, responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		apiAddressService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping
	public AddressDto displayAddress() {
		return apiAddressService.displayAddress();
	}
}
