package com.template;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import com.template.entities.user.CustomUser;
import com.template.entities.user.payloads.UserPayload;
import com.template.entities.user.repository.UserRepository;
import com.template.entities.user.services.UserService;

@Component
@Order(0)
public class UserRunner implements CommandLineRunner {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	private PasswordEncoder bcrypt;

	@Override
	public void run(String... args) throws Exception {

		List<CustomUser> userDB = userRepo.findAll();
		Faker faker = new Faker(new Locale("it"));
		if (userDB.isEmpty()) {

			// ADMIN
			String email = "admin@admin.com";
			String password = "1234";
			String name = "ADMIN";
			String surname = "ADMIN";
			UserPayload payload = new UserPayload(name, surname, email, password);
			payload.setPassword(bcrypt.encode(payload.getPassword()));
			userService.createAdmin(payload);
			
			//USER			
			for (int j = 0; j < 5; j++) {
				String emailU = faker.internet().emailAddress();
				String passwordU = "5678";
				String nameU = faker.name().firstName();
				String surnameU = faker.name().lastName();
				UserPayload payloadU = new UserPayload(nameU, surnameU, emailU, passwordU);
				payloadU.setPassword(bcrypt.encode(payloadU.getPassword()));
				userService.createUser(payloadU);
			}
		}
	}

}
