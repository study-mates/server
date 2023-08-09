package org.studymate.domain.study.dto;

import org.studymate.domain.study.entity.Attendance;
import org.studymate.domain.study.response.AttendanceListResponse;

import lombok.Data;

@Data
public class SimpleAttendance {

	private Long userId;
	private String username;
	private String profileImage;
	private Boolean master;

	public SimpleAttendance(Attendance entity) {
		this.userId = entity.getUser().getId();
		this.username = entity.getUser().getUsername();
		this.profileImage = entity.getUser().getProfileImage();
		this.master = entity.isOwner();
	}

}
