package org.studymate.domain.auth.request;

import lombok.Data;

@Data
public class SignUpRequest {
	private String username;
	private String profileImage;
	private boolean alarm;
}
