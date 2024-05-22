package com.nofrontier.book.domain.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import com.nofrontier.book.api.v1.controller.AddressRestController;
import com.nofrontier.book.domain.exceptions.AddressNotFoundException;
import com.nofrontier.book.domain.exceptions.CityNotFoundException;
import com.nofrontier.book.domain.exceptions.ResourceNotFoundException;
import com.nofrontier.book.domain.model.Address;
import com.nofrontier.book.domain.model.City;
import com.nofrontier.book.domain.repository.AddressRepository;
import com.nofrontier.book.domain.repository.CityRepository;
import com.nofrontier.book.dto.v1.requests.AddressRequest;
import com.nofrontier.book.dto.v1.responses.AddressResponse;
import com.nofrontier.book.utils.SecurityUtils;

public class ApiAddressService {
	
	private Logger logger = Logger.getLogger(ApiUserService.class.getName());

	@Autowired
	PagedResourcesAssembler<AddressResponse> assembler;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private SecurityUtils securityUtils;

	// -------------------------------------------------------------------------------------------------------------

	public PagedModel<EntityModel<AddressResponse>> findAll(Pageable pageable) {
		logger.info("Finding all addresses!");
		var addressPage = addressRepository.findAll(pageable);
		var addressDtoPage = addressPage
				.map(address -> modelMapper.map(address, AddressResponse.class));
		addressDtoPage.forEach(addressResponse -> addressResponse.add(
				linkTo(methodOn(AddressRestController.class).findById(addressResponse.getKey()))
						.withSelfRel()));
		Link link = linkTo(methodOn(AddressRestController.class).findAll(
				pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		return assembler.toModel(addressDtoPage, link);
	}

	// -------------------------------------------------------------------------------------------------------------
   
	public EntityModel<AddressResponse> findById(Long id) {
		logger.info("Finding one address!");
		var entity = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No records found for this ID!"));
		var dto = modelMapper.map(entity, AddressResponse.class);
		dto.add(linkTo(methodOn(AddressRestController.class).findById(id))
				.withSelfRel());
		return EntityModel.of(dto);
	}

	// -------------------------------------------------------------------------------------------------------------

    public EntityModel<AddressResponse> create(AddressRequest addressRequest) {
        Address address = modelMapper.map(addressRequest, Address.class);
        
        // Get city by ID from the request
        Long cityId = addressRequest.getCityId();
        Optional<City> optionalCity = cityRepository.findById(cityId);
        if (optionalCity.isEmpty()) {
            // Handle case when city with provided ID does not exist
            throw new CityNotFoundException("City not found with ID: " + cityId);
        }
        City city = optionalCity.get();
        address.setCity(city);
        
        address = addressRepository.save(address);
        AddressResponse addressResponse = modelMapper.map(address, AddressResponse.class);

     // Adiciona o link self ao AddressResponse
        EntityModel<AddressResponse> addressModel = EntityModel.of(addressResponse);
        Link selfLink = linkTo(methodOn(AddressRestController.class).findById(address.getId())
        ).withSelfRel();
        addressModel.add(selfLink);
        
        return addressModel;

    }
	
	// -------------------------------------------------------------------------------------------------------------

    public AddressResponse update(AddressRequest request) {
        var loggedUser = securityUtils.getLoggedUser();

        var address = modelMapper.map(request, Address.class);
        loggedUser.getPerson().setAddresses(address);

        addressRepository.save(loggedUser);

        return modelMapper.map(loggedUser.getPerson().getAddresses(), null);
    }

    public AddressResponse displayAddress() {
        var loggedUser = securityUtils.getLoggedUser();
        var address = loggedUser.getPerson().getAddresses();

        if (address == null) {
            var message = String.format("User address %s not found", (loggedUser).getEmail());
            throw new AddressNotFoundException(message);
        }
        return modelMapper.map(address, AddressResponse.class);
    }

	
    
}

