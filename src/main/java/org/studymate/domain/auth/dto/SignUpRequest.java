package org.studymate.domain.auth.dto;

import lombok.Data;

@Data
public class SignUpRequest {
	private String username;
	private String profileImg;
	private boolean alarm;
}
