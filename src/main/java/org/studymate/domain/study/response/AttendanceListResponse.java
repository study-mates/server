package org.studymate.domain.study.response;

import java.util.List;

import org.studymate.domain.study.entity.Attendance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceListResponse {
	private String status;
	private String studyId;
	private List<SimpleAttendance> attendance;
	
	@Data
	public static class SimpleAttendance {
		private Long userId;
		private String username;
		private String profileImage;
		private Boolean master;
		public SimpleAttendance(Attendance entity) {
			this.userId =  entity.getUser().getId();
			this.username = entity.getUser().getUsername();
			this.profileImage = entity.getUser().getProfileImage();
			this.master = entity.isOwner();
		}
	}
}
