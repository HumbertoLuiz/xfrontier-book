//package com.nofrontier.book.api.v1.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.learning.core.permissions.EHousekeeperPermissions;
//import com.nofrontier.book.domain.services.ApiMeService;
//import com.nofrontier.book.dto.v1.responses.UserResponse;
//
//@RestController
//@RequestMapping("/api/me")
//public class MeRestController {
//
//    @Autowired
//    private ApiMeService apiMeService;
//
//    @EHousekeeperPermissions.isHousekeeperOrCustomer
//    @GetMapping
//    public UserResponse me() {
//        return apiMeService.getLoggedUser();
//    }
//}
