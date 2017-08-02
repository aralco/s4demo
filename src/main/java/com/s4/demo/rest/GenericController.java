package com.s4.demo.rest;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
public class GenericController<Domain, DTO> {
    @Autowired
    protected ModelMapper modelMapper;

}
