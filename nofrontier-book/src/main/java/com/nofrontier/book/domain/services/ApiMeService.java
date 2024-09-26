package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nofrontier.book.api.v1.controller.MeRestController;
import com.nofrontier.book.dto.v1.UserDto;
import com.nofrontier.book.utils.SecurityUtils;

@Service
public class ApiMeService {
	
	private Logger logger = Logger.getLogger(ApiMeService.class.getName());

    @Autowired
    private SecurityUtils securityUtils;

	@Autowired
	private ModelMapper modelMapper;
    
	@Transactional(readOnly = true)
    public UserDto getLoggedUser() {
		
		logger.info("Finding user logged!");
		
		var userLogged= securityUtils.getLoggedUser();
		// Maps the saved entity to BookResponse
		UserDto userResponse = modelMapper.map(userLogged, UserDto.class);
		userResponse.add(linkTo(methodOn(MeRestController.class)
				.me()).withSelfRel());

		return userResponse;
    }

}
