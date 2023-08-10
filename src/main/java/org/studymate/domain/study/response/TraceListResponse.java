package org.studymate.domain.study.response;

import java.time.LocalDate;
import java.util.List;

import org.studymate.domain.study.dto.SimpleTrace;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraceListResponse {
	private String status;
	private String studyId;
	private LocalDate date;
	private Integer page;
	private List<SimpleTrace> trace;
}
