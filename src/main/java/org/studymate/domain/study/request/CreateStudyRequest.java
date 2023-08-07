package org.studymate.domain.study.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateStudyRequest {
	private String description;
	private LocalDate targetDate;
}
