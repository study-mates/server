package org.studymate.domain.study.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.studymate.domain.user.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
public class Study {
	@Id
	private String id;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	private String description;
	
	private LocalDate openDate;
	private LocalDate closeDate;


	@PrePersist
	public void prePresist() {
		this.id = UUID.randomUUID().toString().split("-")[4];
	}
}
