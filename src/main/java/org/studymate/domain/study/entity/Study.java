package org.studymate.domain.study.entity;

import java.time.LocalDate;

import org.studymate.domain.user.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "studys")
public class Study {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	
	@JsonIgnore
	@ManyToOne
	private User user;
	
	private String description;
	
	private LocalDate openDate;
	private LocalDate closeDate;
}
