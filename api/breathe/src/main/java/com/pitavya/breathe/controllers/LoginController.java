package com.pitavya.breathe.controllers;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pitavya.breathe.model.User;
import com.pitavya.breathe.requestModels.SaveUserRequestModel;
import com.pitavya.breathe.service.SaveUserService;

@RestController
@RequestMapping("/breathe/api")
public class LoginController {

	private static Logger LOGGER = Logger.getLogger(LoginController.class);

	@Autowired
	private SaveUserService saveUserService;

	@RequestMapping(value = "/signin")
	public User saveUser(@RequestBody SaveUserRequestModel requestModel) {
		LOGGER.info("saveUser()");
		return saveUserService.saveUserService(requestModel);
	}

}
