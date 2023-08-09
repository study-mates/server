package org.studymate.domain.study.response;

import java.util.List;

import org.studymate.domain.study.dto.SimpleStudy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudyListResposne {
	private String status;
	private Long userId;
	
	private List<SimpleStudy> study; 
	
	
}
