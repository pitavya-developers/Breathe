package com.pitavya.breathe.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pitavya.breathe.dao.SaveUserDao;
import com.pitavya.breathe.model.User;
import com.pitavya.breathe.requestModels.SaveUserRequestModel;
import com.pitavya.breathe.service.SaveUserService;
import com.pitavya.breathe.utils.BreatheErrorConstants;

@Service
public class SaveUserServiceImpl implements SaveUserService {

	@Autowired
	private SaveUserDao saveUserDao;

	@Override
	public User saveUserService(SaveUserRequestModel requestModel) {

		User user = new User();
		user.setUserName(requestModel.getUserName());
		user.setEmailId(requestModel.getEmailId());
		user.setProvidedId(requestModel.getProvidedId());
		user.setProfilePic(requestModel.getProfilePicUri());

		User savedUser = saveUserDao.saveUser(user);

		if (savedUser == null)
			throw new RuntimeException(BreatheErrorConstants.USER_NOT_SAVED);

		return savedUser;
	}

}
