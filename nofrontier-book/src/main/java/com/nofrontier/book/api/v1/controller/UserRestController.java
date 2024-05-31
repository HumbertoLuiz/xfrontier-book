package com.nofrontier.book.api.v1.controller;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nofrontier.book.domain.services.ApiUserService;
import com.nofrontier.book.dto.v1.requests.UpdateUserRequest;
import com.nofrontier.book.dto.v1.requests.UserRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;
import com.nofrontier.book.dto.v1.responses.UserResponse;
import com.nofrontier.book.utils.MediaType;

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
@RequestMapping("/api/users/v1")
@Tag(name = "Users", description = "Endpoints for Managing Users")
public class UserRestController {

	private final ApiUserService apiUserService;

	// ------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds a User", description = "Finds a User", tags = {
			"User"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<EntityModel<UserResponse>> findById(
			@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok(apiUserService.findById(id));
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping(produces = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
	@Operation(summary = "Finds all Users", description = "Finds all Users", tags = {
			"Users"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<PagedModel<EntityModel<UserResponse>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "12") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC
				: Direction.ASC;

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sortDirection, "completeName"));
		return ResponseEntity.ok(apiUserService.findAll(pageable));
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@CrossOrigin(origins = {"http://localhost:8080",
			"https://nofrontier.com.br"})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Adds a new User", description = "Adds a new User by passing in a JSON, XML or YML representation of the book!", tags = {
			"Books"}, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserResponse.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public UserResponse create(@RequestBody @Valid UserRequest userRequest) {
		var response = apiUserService.create(userRequest);
		return response;
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@PutMapping(consumes = {MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {
					MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
					MediaType.APPLICATION_YML})
	@Operation(summary = "Updates a User", description = "Updates a User by passing in a JSON, XML or YML representation of the User!", tags = {
			"Users"}, responses = {
					@ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public MessageResponse update(@RequestBody @Valid Long id,	UpdateUserRequest updateUserRequest) {
		return apiUserService.update(id, updateUserRequest);
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@PostMapping("/fhoto")
	public MessageResponse updateUserPicture(
			@RequestPart("user_picture") MultipartFile userPicture) {
		return apiUserService.updateUserPicture(userPicture);
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a User", description = "Deletes a User by passing in a JSON, XML or YML representation of the User!", tags = {
			"Users"}, responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		apiUserService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------------------------

}
