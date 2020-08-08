package com.pitavya.breathe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@RequestMapping(value = "/test")
	public String testApi() {
		return "Test SuccessFull for Pitavya-Breathe Api";
	}

}
