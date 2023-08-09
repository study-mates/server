package org.studymate.domain.study.response;

import java.util.List;

import org.studymate.domain.study.dto.SimpleAttendance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceListResponse {
	private String status;
	private String studyId;
	private List<SimpleAttendance> attendance;
	
	
}
