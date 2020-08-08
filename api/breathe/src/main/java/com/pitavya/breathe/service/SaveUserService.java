package com.pitavya.breathe.service;

import com.pitavya.breathe.model.User;
import com.pitavya.breathe.requestModels.SaveUserRequestModel;
import com.pitavya.breathe.responseModels.SaveUserResponseModel;

public interface SaveUserService {

	User saveUserService(SaveUserRequestModel requestModel);

}
