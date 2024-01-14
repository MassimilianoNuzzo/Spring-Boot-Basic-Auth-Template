package com.template.entities.user.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.template.entities.user.Role;
import com.template.entities.user.CustomUser;
import com.template.entities.user.payloads.UserPayload;
import com.template.entities.user.payloads.UserUpdatePayload;
import com.template.entities.user.repository.UserRepository;
import com.template.exceptions.BadRequestException;
import com.template.exceptions.NotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	public CustomUser createAdmin(UserPayload payload) {

		userRepo.findByEmail(payload.getEmail()).ifPresent(user -> {
			throw new BadRequestException("Email " + user.getEmail() + " già utilizzata!");
		});

		CustomUser u = new CustomUser(payload.getName(), payload.getSurname(), payload.getEmail(), payload.getPassword(),
				Role.ADMIN);
		CustomUser userSalvato = userRepo.save(u);

		return userSalvato;
	}

	public CustomUser createUser(UserPayload payload) {

		userRepo.findByEmail(payload.getEmail()).ifPresent(user -> {
			throw new BadRequestException("Email " + user.getEmail() + " già utilizzata!");
		});

		CustomUser u = new CustomUser(payload.getName(), payload.getSurname(), payload.getEmail(), payload.getPassword(),
				Role.USER);
		CustomUser userSalvato = userRepo.save(u);

		return userSalvato;
	}

	public CustomUser findByEmail(String email) {
		return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User non trovato!"));
	}

	public CustomUser findById(String id) {
		return userRepo.findById(UUID.fromString(id))
				.orElseThrow(() -> new NotFoundException("User con id: " + id + " non trovato!"));
	}

	public void aggiungiAdmin(String email) {
		CustomUser found = this.findByEmail(email);
		found.setRole(Role.ADMIN);
		userRepo.save(found);
	}

	public List<CustomUser> find() {
		return userRepo.findAll();
	}

	public Page<CustomUser> findAll(int page, String ordinamento) {
		Pageable pagina = PageRequest.of(page, 10, Sort.by(ordinamento));
		return userRepo.findAll(pagina);
	}

	public Page<CustomUser> findAllUsers(int page, String ordinamento, Role user) {
		Pageable pagina = PageRequest.of(page, 10, Sort.by(ordinamento));
		return userRepo.findAllUsers(pagina, user);
	}

	public CustomUser findByIdAndUpadate(String id, UserUpdatePayload body) {
		CustomUser u = findById(id);
		u.setEmail(body.getEmail());
		u.setName(body.getName());
		u.setSurname(body.getSurname());
		return userRepo.save(u);

	}

	public void findByIdAndDelete(String id) {
		CustomUser u = findById(id);
		userRepo.delete(u);

	}
}
