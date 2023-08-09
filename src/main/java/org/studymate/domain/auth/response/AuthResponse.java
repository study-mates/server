package org.studymate.domain.auth.response;

import org.studymate.domain.user.entity.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
	private String token;
	
	private SimpleUser user;

	@JsonProperty("isNew")
	private boolean isNew;
	
	@Data
	public static class SimpleUser {
		public String username;
		public String profileImage;
		public String lastAccessedStudyId;
		
		public SimpleUser(User entity) {
			this.username = entity.getUsername();
			this.profileImage = entity.getProfileImage();
			this.lastAccessedStudyId = entity.getLastAccessedStudy() != null ? entity.getLastAccessedStudy().getId() : null;
		}
	}
	
}
