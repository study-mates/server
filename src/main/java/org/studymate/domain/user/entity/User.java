package org.studymate.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	private Long id;
	@Column
	private String username;
	@Column
	private String profileImage;
	@Column
	private boolean alarm;
	@Column
	private String provider;
	@Column
	private String accessToken;
	@Column
	private String refreshToken;

}
