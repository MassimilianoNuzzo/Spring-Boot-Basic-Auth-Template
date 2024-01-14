package com.template.entities.user.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdatePayload {
	private String name;
	private String surname;
	private String email;
}
