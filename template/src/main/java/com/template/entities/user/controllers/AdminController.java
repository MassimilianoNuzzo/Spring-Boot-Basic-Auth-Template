package com.template.entities.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.entities.user.CustomUser;
import com.template.entities.user.payloads.UserPayload;
import com.template.entities.user.services.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder bcrypt;

	@PostMapping("")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public CustomUser registerAdmin(@RequestBody @Validated UserPayload payload) {
		payload.setPassword(bcrypt.encode(payload.getPassword()));
		return userService.createAdmin(payload);
	}
	
}
