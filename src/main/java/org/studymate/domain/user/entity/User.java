package org.studymate.domain.user.entity;

import org.studymate.domain.study.entity.Study;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private String provider;
	@Column
	private String accessToken;
	@Column
	private String refreshToken;
	
	@JoinColumn
	@ManyToOne
	private Study lastAccessedStudy;

}
