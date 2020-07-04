package com.adniyo.checkam.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomerController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> landing() {

		return new ResponseEntity<String>("We got here!", new HttpHeaders(), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index"; 
	}
}
