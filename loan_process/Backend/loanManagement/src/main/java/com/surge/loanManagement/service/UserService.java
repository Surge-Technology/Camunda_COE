package com.surge.loanManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surge.loanManagement.model.User;
import com.surge.loanManagement.repository.RoleRepository;
import com.surge.loanManagement.repository.UserDetailsRepository;

@Service
public class UserService {
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	RoleRepository roleRepository;

	   public User createUser(User user) {

	        return userDetailsRepository.save(user);
	    }


	    public List<User> getAllUsers() {
	        return userDetailsRepository.findAll();
	    }

	    public User getUserById(long userId) {
	        Optional<User> user = userDetailsRepository.findById(userId);
	        return user.orElseThrow(() -> new RuntimeException("User not found"));
	    }

	    public User updateUser(long userId, User updatedUser) {
	        User existingUser = getUserById(userId);
	        existingUser.setUsername(updatedUser.getUsername());
	        existingUser.setPassword(updatedUser.getPassword());
	        existingUser.setEmail(updatedUser.getEmail());
	        existingUser.setRole(updatedUser.getRole());
	        return userDetailsRepository.save(existingUser);
	    }

	    public void deleteUser(long userId) {
	    	userDetailsRepository.deleteById(userId);
	    }
}
