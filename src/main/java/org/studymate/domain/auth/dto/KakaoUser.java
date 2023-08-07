package org.studymate.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoUser {
	@JsonIgnore
	private Long id;
	
	private String username;
	private String profileImage;
	
	
	@JsonIgnore
	private boolean alarm;
	@JsonIgnore
	private String accessToken;

	@JsonIgnore
	private boolean isNew;
}
