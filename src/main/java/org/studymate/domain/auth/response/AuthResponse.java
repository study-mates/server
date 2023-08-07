package org.studymate.domain.auth.response;

import org.studymate.domain.auth.dto.KakaoUser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
	private String token;
	private KakaoUser user;
}
