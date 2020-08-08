package com.pitavya.breathe.repo;

import org.springframework.data.repository.CrudRepository;

import com.pitavya.breathe.model.User;

public interface UserRepo extends CrudRepository<User, Long> {

}
