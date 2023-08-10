package org.studymate.domain.study.dto;

import java.util.List;

import org.studymate.domain.study.entity.Trace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SimpleTrace {
	private Long traceId;
	private String title;
	private String writer;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String description;
	
	private String mainImage;
	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty(value = "allImages")
	private List<String> images;
	
	public SimpleTrace(Trace entity) {
		this.traceId = entity.getId();
		this.title = entity.getTitle();
		this.writer = entity.getWriter().getUsername();
		this.description = entity.getDescription();
		if(entity.getImages() == null || entity.getImages().isEmpty()) {
			this.images = List.of();
		}else {
			this.images = entity.getImages().stream().map(t->t.getUrl()).toList();
			mainImage = this.images.get(0);
		}
//		mainImage=null;
	}
}
