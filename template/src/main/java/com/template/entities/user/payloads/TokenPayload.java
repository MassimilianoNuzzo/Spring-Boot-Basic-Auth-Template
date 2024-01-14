package com.template.entities.user.payloads;

import com.template.entities.user.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPayload {
	private String JwtToken;
	private CustomUser user;
}
