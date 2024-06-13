package com.nofrontier.book.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.nofrontier.book.domain.exceptions.AddressServiceException;
import com.nofrontier.book.domain.exceptions.BusinessException;
import com.nofrontier.book.domain.exceptions.ConsultCityServiceException;
import com.nofrontier.book.domain.exceptions.EntityInUseException;
import com.nofrontier.book.domain.exceptions.GatewayPaymentException;
import com.nofrontier.book.domain.exceptions.InvalidJwtAuthenticationException;
import com.nofrontier.book.domain.exceptions.OrderNotFoundException;
import com.nofrontier.book.domain.exceptions.ProductNotFoundException;
import com.nofrontier.book.domain.exceptions.RequiredObjectIsNullException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.exceptions.TokenBlackListException;
import com.nofrontier.book.domain.exceptions.TokenServiceException;
import com.nofrontier.book.domain.exceptions.UserNotFoundException;
import com.nofrontier.book.domain.exceptions.ValidatingException;
import com.nofrontier.book.dto.v1.responses.ErrorResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
public class CustomizedResponseEntityExceptionHandler
		extends
			ResponseEntityExceptionHandler {

	private SnakeCaseStrategy camelCaseToSnakeCase = new SnakeCaseStrategy();

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception exception,
			HttpServletRequest request) {
		return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Object> handleBusinessException(
			BusinessException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.BAD_REQUEST,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(
			DataIntegrityViolationException exception,
			HttpServletRequest request) {
		return createErrorResponse(HttpStatus.CONFLICT,
				"Data integrity violation: " + exception.getLocalizedMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(EntityInUseException.class)
	public ResponseEntity<Object> handleEntityInUseException(
			EntityInUseException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.CONFLICT,
				"Entity in use: " + exception.getLocalizedMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(
			ResourceNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(RequiredObjectIsNullException.class)
	public ResponseEntity<Object> handleBadRequestExceptions(
			RequiredObjectIsNullException exception,
			HttpServletRequest request) {
		return createErrorResponse(HttpStatus.BAD_REQUEST,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public ResponseEntity<Object> handleInvalidJwtAuthenticationExceptions(
			InvalidJwtAuthenticationException exception,
			HttpServletRequest request) {
		return createErrorResponse(HttpStatus.FORBIDDEN,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(TokenServiceException.class)
	public ResponseEntity<Object> handleTokenServiceException(
			TokenServiceException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.UNAUTHORIZED,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(
			EntityNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(
			UserNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(AddressServiceException.class)
	public ResponseEntity<Object> handleAddressServiceException(
			AddressServiceException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.BAD_REQUEST,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(TokenBlackListException.class)
	public ResponseEntity<Object> handleTokenBlackListException(
			TokenBlackListException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.UNAUTHORIZED,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ConsultCityServiceException.class)
	public ResponseEntity<Object> handleConsultCityServiceException(
			ConsultCityServiceException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(GatewayPaymentException.class)
	public ResponseEntity<Object> handleGatewayPaymentException(
			GatewayPaymentException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.BAD_REQUEST,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Object> handleOrderNotFoundException(
			OrderNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<Object> handleProductNotFoundException(
			ProductNotFoundException exception, HttpServletRequest request) {
		return createErrorResponse(HttpStatus.NOT_FOUND,
				exception.getLocalizedMessage(), request.getRequestURI());
	}

    @ExceptionHandler(ValidatingException.class)
    public ResponseEntity<Object> handleValidatingException(ValidatingException exception) {
        Map<String, List<String>> body = new HashMap<>();
        var fieldError = exception.getFieldError();
        var fieldErrors = new ArrayList<String>();
        fieldErrors.add(fieldError.getDefaultMessage());
        var field = camelCaseToSnakeCase.translate(fieldError.getField());
        body.put(field, fieldErrors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            var field = camelCaseToSnakeCase.translate(fieldError.getField());
            body.computeIfAbsent(field, k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        });

        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            var field = camelCaseToSnakeCase.translate(fieldError.getField());
            body.computeIfAbsent(field, k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        });

        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createErrorResponse(HttpStatus status, String message, String path) {
        var errorResponse = ErrorResponse.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .message(message)
                .path(path)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, status);
    }

}