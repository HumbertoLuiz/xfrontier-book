//package com.nofrontier.book.api.v1.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.learning.core.services.checkaddress.adapters.AddressService;
//import com.learning.core.services.checkaddress.dtos.AddressResponse;
//
//@RestController
//@RequestMapping("/api/address")
//public class AddressRestController {
//
//    @Autowired
//    private AddressService service;
//
//    @GetMapping
//    public AddressResponse findAddressByzipCode(@RequestParam(required= false) String zipCode) {
//        return service.findAddressByzipCode(zipCode);
//    }
//}
