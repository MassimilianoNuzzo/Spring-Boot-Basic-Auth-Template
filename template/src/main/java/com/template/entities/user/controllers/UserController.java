package com.template.entities.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.template.entities.user.Role;
import com.template.entities.user.payloads.UserUpdatePayload;
import com.template.entities.user.CustomUser;
import com.template.entities.user.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;

	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public Page<CustomUser> findAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "id") String order) {
		return userService.findAllUsers(page, order, Role.USER);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public CustomUser findById(@PathVariable String id) {
		return userService.findById(id);
	}
	
	@GetMapping("/me")
	public CustomUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUser user = (CustomUser) authentication.getPrincipal();
		return userService.findById(user.getId().toString());
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public CustomUser updateById(@PathVariable String id, @RequestBody UserUpdatePayload payload) {
		return userService.findByIdAndUpadate(id, payload);
	}
	
	@PutMapping("/me")
	public CustomUser updateCurrentUser(@RequestBody UserUpdatePayload payload) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUser user = (CustomUser) authentication.getPrincipal();
		return userService.findByIdAndUpadate(user.getId().toString(),payload);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@ResponseStatus(HttpStatus.OK)
	public void deleteById(@PathVariable String id) {
		userService.findByIdAndDelete(id);
	}
}
