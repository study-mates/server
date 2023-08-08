package org.studymate.domain.study.response;

import java.time.LocalDate;
import java.util.List;

import org.studymate.domain.study.entity.Notice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeListResponse {
	
	private String status;
	private String studyId;
	private List<SimpleNotice> notice;
	
	@Data
	public static class SimpleNotice {
		private String tag;
		private String description;
		private LocalDate writed;
		public SimpleNotice(Notice entity) {
			this.tag = entity.getTag();
			this.description = entity.getDescription();
			this.writed = entity.getWrited().toLocalDate();
		}
	}
}


