package com.devSoft.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devSoft.Model.User;
import com.devSoft.Repository.UserRepository;
import com.devSoft.Service.UserService;

@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void signup(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepo.save(user);

	}

	@Override
	public User login(String mail, String psw) {

		return userRepo.findFirstByEmail(mail)
				.filter(user -> passwordEncoder.matches(psw, user.getPassword()))
				.orElse(null);
	}

	@Override
	public boolean emailExists(String email) {
		return userRepo.findFirstByEmail(email).isPresent();
	}

}
