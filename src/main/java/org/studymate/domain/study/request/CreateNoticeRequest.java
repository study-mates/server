package org.studymate.domain.study.request;

import lombok.Data;

@Data
public class CreateNoticeRequest {
	private String description;
	private String tag;
}
