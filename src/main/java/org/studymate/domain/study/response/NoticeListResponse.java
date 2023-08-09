package org.studymate.domain.study.response;

import java.util.List;

import org.studymate.domain.study.dto.SimpleNotice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoticeListResponse {
	
	private String status;
	private String studyId;
	private List<SimpleNotice> notice;
	
	
}


