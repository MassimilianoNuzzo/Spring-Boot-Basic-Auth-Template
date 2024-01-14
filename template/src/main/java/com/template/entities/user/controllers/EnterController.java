package com.template.entities.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.entities.user.CustomUser;
import com.template.entities.user.payloads.UserPayload;
import com.template.entities.user.payloads.LoginPayload;
import com.template.entities.user.payloads.TokenPayload;
import com.template.entities.user.services.UserService;
import com.template.exceptions.UnauthorizedException;
import com.template.security.JwtTools;

@RestController
@RequestMapping("/auth")
public class EnterController {

	@Autowired
	UserService userService;

	@Autowired
	private PasswordEncoder bcrypt;

	@PostMapping("/login")
	public ResponseEntity<TokenPayload> login(@RequestBody @Validated LoginPayload body) {

		CustomUser user = userService.findByEmail(body.getEmail());

		String plainPW = body.getPassword();
		String hashedPW = user.getPassword();

		if (!bcrypt.matches(plainPW, hashedPW))
			throw new UnauthorizedException("Credenziali non valide");

		String token = JwtTools.createToken(user);

		return new ResponseEntity<>(new TokenPayload(token, user), HttpStatus.OK);
	}

	@PostMapping("")
	public CustomUser register(@RequestBody @Validated UserPayload payload) {
		payload.setPassword(bcrypt.encode(payload.getPassword()));
		return userService.createUser(payload);
	}
}
