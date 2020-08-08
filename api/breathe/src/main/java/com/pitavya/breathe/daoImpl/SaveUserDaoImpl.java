package com.pitavya.breathe.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pitavya.breathe.dao.SaveUserDao;
import com.pitavya.breathe.model.User;
import com.pitavya.breathe.repo.UserRepo;

@Repository
public class SaveUserDaoImpl implements SaveUserDao {

	@Autowired
	private UserRepo userRepo;

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

}
