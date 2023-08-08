package org.studymate.domain.study.response;

import java.util.List;

import org.studymate.domain.study.entity.Attendance;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class StudyListResposne {
	private String status;
	private Long userId;
	
	private List<SimpleStudy> study; 
	
	@Data
	public static class SimpleStudy {
		private String studyId;
		private Long studyLeadUserId;
		private String description;
		private String role;
		
		public SimpleStudy(Attendance entity, Long userId) {
			this.studyId = entity.getStudy().getId();
			this.studyLeadUserId = entity.getStudy().getUser().getId();
			this.description = entity.getStudy().getDescription();
			this.role = entity.getStudy().getUser().getId().equals(userId) ? "master" : "guest";
			log.debug("SimpleStudy {} {} {}", entity.getStudy().getUser().getId() , userId , this.role);
		}
	}
}
