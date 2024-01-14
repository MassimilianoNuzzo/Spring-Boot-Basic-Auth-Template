package com.template.entities.user;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({ "password", "username", "accountNonExpired", "accountNonLocked", "enabled",
		"credentialsNonExpired", "authorities" })
@Table(name = "users")

public class CustomUser implements UserDetails {

	@Id
	@GeneratedValue
	private UUID id;
	private String name;
	private String surname;
	private String email;
	private String password;
	private LocalDate registrationDate;
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private boolean isEnabled;
	private boolean isCredentialsNonExpired;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;

	public CustomUser(String name, String surname, String email, String password, Role role) {
		setName(name);
		setSurname(surname);
		setEmail(email);
		setPassword(password);
		setRegistrationDate(LocalDate.now());
		setRole(role);
		setEnabled(true);
		setAccountNonExpired(true);
		setCredentialsNonExpired(true);
		setAccountNonLocked(true);
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}
}
