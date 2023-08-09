package org.studymate.domain.study.dto;

import org.studymate.domain.study.entity.Trace;

import lombok.Builder;
import lombok.Data;

@Data
public class SimpleTrace {
	private String title;
	private String writer;
	private String mainImageURL;
	
	public SimpleTrace(Trace entity) {
		this.title = entity.getTitle();
		this.writer = entity.getWriter().getUsername();
		this.mainImageURL = null;
	}
}
