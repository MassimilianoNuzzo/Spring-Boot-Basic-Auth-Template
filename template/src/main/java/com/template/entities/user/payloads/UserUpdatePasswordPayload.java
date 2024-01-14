package com.template.entities.user.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdatePasswordPayload {
	private String email;
	private String currentPassword;
	private String newPassword;
}
