package org.studymate.domain.study.dto;

import java.time.LocalDate;

import org.studymate.domain.study.entity.Notice;
import org.studymate.domain.study.response.NoticeListResponse;

import lombok.Data;

@Data
public  class SimpleNotice {
	private String tag;
	private String description;
	private LocalDate writed;
	public SimpleNotice(Notice entity) {
		this.tag = entity.getTag();
		this.description = entity.getDescription();
		this.writed = entity.getWrited().toLocalDate();
	}
}
