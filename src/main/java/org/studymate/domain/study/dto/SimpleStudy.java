package org.studymate.domain.study.dto;

import java.time.LocalDate;

import org.studymate.domain.study.entity.Study;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleStudy {
	private String studyId;
	private Boolean enabled;
	private Long studyLeadUserId;
	private String description;
	
	private String role;
	private LocalDate openDate;

	public SimpleStudy(Study entity) {
		this.studyId = entity.getId();
		this.studyLeadUserId = entity.getUser().getId();
		this.description = entity.getDescription();
		this.enabled = entity.getCloseDate() == null;
		this.openDate = entity.getOpenDate();
	}
}
