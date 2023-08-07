package org.studymate.domain.auth.response;

import org.studymate.domain.auth.dto.KakaoUser;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
	private String token;
	
	@JsonProperty("isNew")
	private boolean isNew;
	private KakaoUser user;
	
}
