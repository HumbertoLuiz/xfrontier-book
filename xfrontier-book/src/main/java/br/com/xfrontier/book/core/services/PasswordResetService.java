package br.com.xfrontier.book.core.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.xfrontier.book.domain.exceptions.ResourceNotFoundException;
import br.com.xfrontier.book.api.v1.controller.ResetPasswordRestController;
import br.com.xfrontier.book.domain.exceptions.PasswordResetNotFound;
import br.com.xfrontier.book.domain.model.PasswordReset;
import br.com.xfrontier.book.domain.repository.PasswordResetRepository;
import br.com.xfrontier.book.domain.repository.UserRepository;
import br.com.xfrontier.book.dto.v1.PasswordResetResponse;

@Service
public class PasswordResetService {
	
	 private Logger logger = Logger.getLogger(PasswordResetService.class.getName());

	@Autowired
    private PasswordResetRepository passwordResetRepository;

	@Autowired
    private UserRepository userRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;

	// -------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public PasswordResetResponse findById(Long id) {
        logger.info("Finding PasswordReset!");
        var entity = passwordResetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        PasswordResetResponse passwordResetResponse = modelMapper.map(entity, PasswordResetResponse.class);
        passwordResetResponse.add(linkTo(methodOn(ResetPasswordRestController.class)
                .findById(passwordResetResponse.getKey())).withSelfRel());

        return passwordResetResponse;
    }
    
	// -------------------------------------------------------------------------------------------------------------
    
    @Transactional
    public EntityModel<PasswordResetResponse> createPasswordReset(String username) {
        if (userRepository.existsByEmail(username)) {
            var passwordReset = PasswordReset.builder().email(username)
                    .token(UUID.randomUUID().toString()).build();
            PasswordReset savedPasswordReset = passwordResetRepository.save(passwordReset);

            PasswordResetResponse response = modelMapper.map(savedPasswordReset, PasswordResetResponse.class);

            // Add links HAL
            EntityModel<PasswordResetResponse> resource = EntityModel.of(response);
            Link selfLink = linkTo(methodOn(ResetPasswordRestController.class)
                    .getPasswordReset(savedPasswordReset.getId())).withSelfRel();
            resource.add(selfLink);

            return resource;
        }
        return null;
    }

	// -------------------------------------------------------------------------------------------------------------

    @Transactional
    public void resetPassword(String passwordResetToken, String newPassword) {
        var passwordReset = findPasswordResetByToken(passwordResetToken);
        var user = userRepository.findByEmail(passwordReset.getEmail()).get();
        var newPasswordHash = passwordEncoder.encode(newPassword);
        user.setPassword(newPasswordHash);
        userRepository.save(user);
        passwordResetRepository.delete(passwordReset);
    }

	// -------------------------------------------------------------------------------------------------------------

    @Transactional(readOnly = true)
    private PasswordReset findPasswordResetByToken(String passwordResetToken) {
        return passwordResetRepository.findByToken(passwordResetToken).orElseThrow(PasswordResetNotFound::new);
    }
}
