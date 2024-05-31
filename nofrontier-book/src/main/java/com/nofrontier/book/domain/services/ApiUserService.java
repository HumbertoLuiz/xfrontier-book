package com.nofrontier.book.domain.services;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.nofrontier.book.api.v1.controller.UserRestController;
import com.nofrontier.book.core.publishers.NewUserPublisher;
import com.nofrontier.book.core.services.storage.adapters.StorageService;
import com.nofrontier.book.core.services.token.providers.JjwtService;
import com.nofrontier.book.core.validation.UserValidator;
import com.nofrontier.book.domain.exceptions.IncorrectPasswordException;
import com.nofrontier.book.domain.exceptions.PasswordDoesntMatchException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.User;
import com.nofrontier.book.domain.repository.UserRepository;
import com.nofrontier.book.dto.v1.requests.UpdateUserRequest;
import com.nofrontier.book.dto.v1.requests.UserRequest;
import com.nofrontier.book.dto.v1.responses.MessageResponse;
import com.nofrontier.book.dto.v1.responses.TokenResponse;
import com.nofrontier.book.dto.v1.responses.UserRegisterResponse;
import com.nofrontier.book.dto.v1.responses.UserResponse;
import com.nofrontier.book.utils.SecurityUtils;

@Service
public class ApiUserService {

	private Logger logger = Logger.getLogger(ApiUserService.class.getName());

	@Autowired
	PagedResourcesAssembler<UserResponse> assembler;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserValidator validator;

	@Autowired
	private StorageService storageService;

	@Autowired
	private NewUserPublisher newUserPublisher;

	@Autowired
	private JjwtService tokenService;

	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private ModelMapper modelMapper;

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public EntityModel<UserResponse> findById(Long id) {
		logger.info("Finding one user!");
		var entity = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var dto = modelMapper.map(entity, UserResponse.class);
		dto.add(linkTo(methodOn(UserRestController.class).findById(id))
				.withSelfRel());
		return EntityModel.of(dto);
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional(readOnly = true)
	public PagedModel<EntityModel<UserResponse>> findAll(Pageable pageable) {
		logger.info("Finding all users!");
		var userPage = userRepository.findAll(pageable);
		var userDtoPage = userPage
				.map(user -> modelMapper.map(user, UserResponse.class));
		userDtoPage.map(userResponse -> userResponse
				.add(linkTo(methodOn(UserRestController.class)
						.findById(userResponse.getKey())).withSelfRel()));
		Link link = linkTo(methodOn(UserRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(userDtoPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------

	public UserResponse create(UserRequest request) {
		validatePasswordConfirmation(request);

		var userToSave = modelMapper.map(request, User.class);

		// Performs user validation
		validator.validate(userToSave);

		// Encrypts the user's password
		var passwordEncrypted = passwordEncoder
				.encode(userToSave.getPassword());
		userToSave.setPassword(passwordEncrypted);

		// Saves the user's document image
		var documentPicture = storageService.save(request.getDocumentPicture());
		userToSave.setDocumentPicture(documentPicture);

		// Saves the user in the repository
		var userSaved = userRepository.save(userToSave);
		// Publish the new user event
		newUserPublisher.publish(userSaved);

		// Maps the saved user to UserResponse
		var response = modelMapper.map(userSaved, UserRegisterResponse.class);
		// Generates the response token and sets it in UserResponse
		var tokenResponse = generateTokenResponse(response);
		response.setToken(tokenResponse);

		// Adds the self link to UserResponse
		response.add(linkTo(
				methodOn(UserRestController.class).findById(userSaved.getId()))
				.withSelfRel());

		return response;
	}

	// -------------------------------------------------------------------------------------------------------------

	@Transactional
	public MessageResponse updateUserPicture(MultipartFile userPicture) {
		var loggedUser = securityUtils.getLoggedUser();
		var picture = storageService.save(userPicture);
		loggedUser.setUserPicture(picture);
		userRepository.save(loggedUser);
		return new MessageResponse("Photo saved successfully!");
	}

	@Transactional
	public MessageResponse update(Long id, UpdateUserRequest updateUserRequest) {
		if (updateUserRequest == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating one User!");

		var loggedUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		
		loggedUser = securityUtils.getLoggedUser();
		updateInformationloggedUser(updateUserRequest, loggedUser);
		validator.validate(loggedUser);
		changePassword(updateUserRequest, loggedUser);
		userRepository.save(loggedUser);
		return new MessageResponse("User successfully updated");
	}

	// -------------------------------------------------------------------------------------------------------------

	public void delete(Long id) {
		logger.info("Deleting one User!");
		var entity = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		userRepository.delete(entity);
	}
	
	// -------------------------------------------------------------------------------------------------------------

	private void changePassword(UpdateUserRequest request, User loggedUser) {
		var hasPasswords = request.getPassword() != null
				&& request.getNewPassword() != null
				&& request.getPasswordConfirmation() != null;

		if (hasPasswords) {
			checkPassword(request, loggedUser);
			validatePasswordConfirmation(request);
			var newPassword = request.getNewPassword();
			var newPasswordHash = passwordEncoder.encode(newPassword);
			loggedUser.setPassword(newPasswordHash);
		}
	}

	private void checkPassword(UpdateUserRequest request, User loggedUser) {
		var passwordRequest = request.getPassword();
		var passwordDB = loggedUser.getPassword();

		if (!passwordEncoder.matches(passwordRequest, passwordDB)) {
			var message = "The password you entered is incorrect";
			var fieldError = new FieldError(request.getClass().getName(),
					"password", passwordRequest, false, null, null, message);
			throw new IncorrectPasswordException(message, fieldError);
		}
	}

	private void updateInformationloggedUser(UpdateUserRequest request,
			User loggedUser) {
		// Updates direct user information
		loggedUser.setCompleteName(firstNonNull(request.getCompleteName(),
				loggedUser.getCompleteName()));
		loggedUser.setEmail(
				firstNonNull(request.getEmail(), loggedUser.getEmail()));

		// Access the Person object associated with the User
		loggedUser.getPerson()
				.setGender(firstNonNull(request.getPerson().getGender(),
						loggedUser.getPerson().getGender()));

		loggedUser.getPerson().setCpf(firstNonNull(request.getPerson().getCpf(),
				loggedUser.getPerson().getCpf()));

		loggedUser.getPerson()
				.setBirth(firstNonNull(request.getPerson().getBirth(),
						loggedUser.getPerson().getBirth()));

		loggedUser.getPerson().setPhoneNumber(
				firstNonNull(request.getPerson().getPhoneNumber(),
						loggedUser.getPerson().getPhoneNumber()));

		loggedUser.getPerson().setMobileNumber(
				firstNonNull(request.getPerson().getMobileNumber(),
						loggedUser.getPerson().getMobileNumber()));

		loggedUser.getPerson()
				.setKeyPix(firstNonNull(request.getPerson().getKeyPix(),
						loggedUser.getPerson().getKeyPix()));

		loggedUser.getPerson()
				.setEnabled(firstNonNull(request.getPerson().getEnabled(),
						loggedUser.getPerson().getEnabled()));
	}

	private TokenResponse generateTokenResponse(UserRegisterResponse response) {
		var token = tokenService.generateAccessToken(response.getEmail());
		var refresh = tokenService.generateRefreshToken(response.getEmail());
		var tokenResponse = new TokenResponse(token, refresh);
		return tokenResponse;
	}

	private void validatePasswordConfirmation(UserRequest request) {
		var password = request.getPassword();
		var passwordConfirmation = request.getPasswordConfirmation();

		if (!password.equals(passwordConfirmation)) {
			var message = "The two password fields don't match";
			var fieldError = new FieldError(request.getClass().getName(),
					"passwordConfirmation", request.getPasswordConfirmation(),
					false, null, null, message);

			throw new PasswordDoesntMatchException(message, fieldError);
		}
	}

	private void validatePasswordConfirmation(UpdateUserRequest request) {
		var password = request.getNewPassword();
		var passwordConfirmation = request.getPasswordConfirmation();

		if (!password.equals(passwordConfirmation)) {
			var message = "The two password fields don't match";
			var fieldError = new FieldError(request.getClass().getName(),
					"passwordConfirmation", request.getPasswordConfirmation(),
					false, null, null, message);

			throw new PasswordDoesntMatchException(message, fieldError);
		}
	}

}