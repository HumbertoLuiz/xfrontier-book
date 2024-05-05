package com.nofrontier.book.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nofrontier.book.core.modelmapper.ModelMapperConfig;
import com.nofrontier.book.dto.v1.responses.UserResponse;
import com.nofrontier.book.utils.SecurityUtils;

@Service
public class ApiMeService {

    @Autowired
    private SecurityUtils securityUtils;

    public UserResponse getLoggedUser() {
        var userLogged= securityUtils.getLoggedUser();
        return ModelMapperConfig.parseObject(userLogged, UserResponse.class);
    }

}
