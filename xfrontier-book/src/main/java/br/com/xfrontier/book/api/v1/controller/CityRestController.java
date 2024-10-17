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

import br.com.xfrontier.book.domain.services.ApiCityService;
import br.com.xfrontier.book.dto.v1.CityDto;
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
@RequestMapping("/api/cities/v1")
@Tag(name = "Cities", description = "Endpoints for Managing Cities")
public class CityRestController {

	private final ApiCityService apiCityService;

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a City", description = "Finds a City", tags = {
			"Cities"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CityDto.class))),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public CityDto findById(@PathVariable(value = "id") Long id) {
		return apiCityService.findById(id);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping(produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds all Cityes", description = "Finds all Cityes", tags = {
			"Cities"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CityDto.class)))}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<PagedModel<EntityModel<CityDto>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC
				: Direction.ASC;

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDirection, "name"));
		return ResponseEntity.ok(apiCityService.findAll(pageable));
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = {"http://localhost:8080",
			"https://xfrontier.com.br"})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Adds a new City", description = "Adds a new City by passing in a JSON, XML or YML representation of the city!", tags = {
			"Cities"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = CityDto.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public CityDto create(@RequestBody @Valid CityDto cityDtoRequest) {
		return apiCityService.create(cityDtoRequest);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a City", description = "Updates a City by passing in a JSON, XML or YML representation of the city!", tags = {
			"Cities"}, responses = {
					@ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = CityDto.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public CityDto update(@PathVariable(value = "id") Long id, @RequestBody @Valid CityDto cityDtoRequest) {
		return apiCityService.update(id, cityDtoRequest);
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a City", description = "Deletes a City by passing in a JSON, XML or YML representation of the city!", tags = {
			"Cities"}, responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		apiCityService.delete(id);
		return ResponseEntity.noContent().build();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------

}
