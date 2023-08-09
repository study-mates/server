package org.studymate.domain.study.response;

import java.time.LocalDate;
import java.util.List;

import org.studymate.domain.study.dto.SimpleNotice;
import org.studymate.domain.study.dto.SimpleStudy;
import org.studymate.domain.study.dto.SimpleTrace;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyInfoResponse {
	
	private String status;
	
	private SimpleStudy study;

	private LocalDate today;
	private Long elapsed;
	private Long attendanceCount;
	
	private List<SimpleNotice> notice;
	
	private List<LocalDate> traceDate;
	private List<SimpleTrace> todaysTrace;
	
	
	
}
