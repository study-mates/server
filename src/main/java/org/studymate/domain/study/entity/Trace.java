package org.studymate.domain.study.entity;

import java.time.LocalDate;
import java.util.List;

import org.studymate.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Trace {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn
	private User writer;
	
	@ManyToOne
	@JoinColumn
	private Study study;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column
	private LocalDate created;

	
	@OneToMany
	@JoinColumn(name="trace_id")
	private List<Image> images;
}
